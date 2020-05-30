/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.caching;

/**
 * Describes an executor that has responses that can be cached. This is used for centralised
 * invalidation of cached content.
 *
 * @author John Grimes
 */
public interface Cacheable {

  /**
   * Invalidates any cached responses that may be stored.
   */
  void invalidateCache();

}
