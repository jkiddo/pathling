/*
 * Copyright © 2018-2021, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.security;

import au.csiro.pathling.errors.AccessDeniedError;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * The aspect that inserts checks relating to security.
 *
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-ataspectj-advice-params">Advice
 * Parameters</a>
 */
@Aspect
@Component
@Profile("core")
@ConditionalOnProperty(prefix = "pathling", name = "auth.enabled", havingValue = "true")
@Slf4j
public class SecurityAspect {

  /**
   * Checks if the current user is authorised to access the resource.
   *
   * @param resourceAccess the resource access required.
   * @param resourceType the resource type.
   * @throws AccessDeniedError if unauthorised.
   */
  @Before("@annotation(resourceAccess) && args(resourceType,..)")
  public void checkResourceRead(@Nonnull final ResourceAccess resourceAccess,
      final ResourceType resourceType) {
    log.debug("Checking access to resource: {}, type: {}", resourceType, resourceAccess.value());
    checkHasAuthority(PathlingAuthority.resourceAccess(resourceAccess.value(), resourceType));
  }

  /**
   * Checks if the current user is authorised to access the operation.
   *
   * @param operationAccess the operation access required.
   * @throws AccessDeniedError if unauthorised.
   */
  @Before("@annotation(operationAccess)")
  public void checkRequiredAuthority(@Nonnull final OperationAccess operationAccess) {
    log.debug("Checking access to operation: {}", operationAccess.value());
    checkHasAuthority(PathlingAuthority.operationAccess(operationAccess.value()));
  }

  private static void checkHasAuthority(@Nonnull final PathlingAuthority requiredAuthority) {
    final Authentication authentication = SecurityContextHolder
        .getContext().getAuthentication();
    final AbstractAuthenticationToken authToken = (authentication instanceof AbstractAuthenticationToken)
                                                  ? (AbstractAuthenticationToken) authentication
                                                  : null;
    if (authToken == null) {
      throw new AccessDeniedError("Token not present");
    }
    final Collection<PathlingAuthority> authorities = authToken.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(authority -> authority.startsWith("pathling"))
        .map(PathlingAuthority::fromAuthority)
        .collect(Collectors.toList());
    if (authToken == null || authToken.getAuthorities() == null || !requiredAuthority
        .subsumedByAny(authorities)) {
      throw new AccessDeniedError(
          String.format("Missing authority: '%s'", requiredAuthority),
          requiredAuthority.getAuthority());
    }
  }
}