"use strict";(self.webpackChunkpathling_site=self.webpackChunkpathling_site||[]).push([[880],{3905:(e,t,r)=>{r.d(t,{Zo:()=>g,kt:()=>d});var a=r(7294);function n(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function i(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,a)}return r}function o(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?i(Object(r),!0).forEach((function(t){n(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function s(e,t){if(null==e)return{};var r,a,n=function(e,t){if(null==e)return{};var r,a,n={},i=Object.keys(e);for(a=0;a<i.length;a++)r=i[a],t.indexOf(r)>=0||(n[r]=e[r]);return n}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)r=i[a],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(n[r]=e[r])}return n}var l=a.createContext({}),p=function(e){var t=a.useContext(l),r=t;return e&&(r="function"==typeof e?e(t):o(o({},t),e)),r},g=function(e){var t=p(e.components);return a.createElement(l.Provider,{value:t},e.children)},u="mdxType",h={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},c=a.forwardRef((function(e,t){var r=e.components,n=e.mdxType,i=e.originalType,l=e.parentName,g=s(e,["components","mdxType","originalType","parentName"]),u=p(r),c=n,d=u["".concat(l,".").concat(c)]||u[c]||h[c]||i;return r?a.createElement(d,o(o({ref:t},g),{},{components:r})):a.createElement(d,o({ref:t},g))}));function d(e,t){var r=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var i=r.length,o=new Array(i);o[0]=c;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s[u]="string"==typeof e?e:n,o[1]=s;for(var p=2;p<i;p++)o[p]=r[p];return a.createElement.apply(null,o)}return a.createElement.apply(null,r)}c.displayName="MDXCreateElement"},190:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>l,contentTitle:()=>o,default:()=>h,frontMatter:()=>i,metadata:()=>s,toc:()=>p});var a=r(7462),n=(r(7294),r(3905));const i={sidebar_position:3,description:"The aggregate operation allows a user to perform aggregate queries on data held within the Pathling FHIR server."},o="Aggregate",s={unversionedId:"server/operations/aggregate",id:"server/operations/aggregate",title:"Aggregate",description:"The aggregate operation allows a user to perform aggregate queries on data held within the Pathling FHIR server.",source:"@site/docs/server/operations/aggregate.md",sourceDirName:"server/operations",slug:"/server/operations/aggregate",permalink:"/docs/server/operations/aggregate",draft:!1,editUrl:"https://github.com/aehrc/pathling/tree/main/site/docs/server/operations/aggregate.md",tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3,description:"The aggregate operation allows a user to perform aggregate queries on data held within the Pathling FHIR server."},sidebar:"server",previous:{title:"Search",permalink:"/docs/server/operations/search"},next:{title:"Extract",permalink:"/docs/server/operations/extract"}},l={},p=[{value:"Request",id:"request",level:2},{value:"Response",id:"response",level:2},{value:"Examples",id:"examples",level:2}],g={toc:p},u="wrapper";function h(e){let{components:t,...i}=e;return(0,n.kt)(u,(0,a.Z)({},g,i,{components:t,mdxType:"MDXLayout"}),(0,n.kt)("h1",{id:"aggregate"},"Aggregate"),(0,n.kt)("p",null,(0,n.kt)("a",{parentName:"p",href:"https://pathling.csiro.au/fhir/OperationDefinition/aggregate-7"},"FHIR OperationDefinition")),(0,n.kt)("p",null,"This operation allows a user to perform aggregate queries on data held within\nthe FHIR server. You call the operation by specifying aggregation, grouping and\nfilter expressions, and grouped results are returned."),(0,n.kt)("p",null,"The aggregate operation is useful for exploratory data analysis, as well as\npowering visualizations and other summarized views of the data. Drill-down\nexpressions returned within the response can be used with the ",(0,n.kt)("a",{parentName:"p",href:"./search"},"search"),"\noperation to retrieve the resources that make up each grouped result."),(0,n.kt)("admonition",{type:"note"},(0,n.kt)("p",{parentName:"admonition"},"The ",(0,n.kt)("inlineCode",{parentName:"p"},"aggregate")," operation supports the ",(0,n.kt)("a",{parentName:"p",href:"../async"},"Asynchronous Request Pattern"),",\nwhich allows you to kick off a long-running request and check on its progress\nusing a status endpoint.")),(0,n.kt)("pre",null,(0,n.kt)("code",{parentName:"pre"},"GET [FHIR endpoint]/[resource type]/$aggregate?[parameters...]\n")),(0,n.kt)("pre",null,(0,n.kt)("code",{parentName:"pre"},"POST [FHIR endpoint]/[resource type]/$aggregate\n")),(0,n.kt)("p",null,(0,n.kt)("img",{alt:"Aggregate",src:r(4638).Z+"#light-mode-only",title:"Aggregate",width:"667",height:"449"}),"\n",(0,n.kt)("img",{alt:"Aggregate",src:r(7244).Z+"#dark-mode-only",title:"Aggregate",width:"667",height:"454"})),(0,n.kt)("h2",{id:"request"},"Request"),(0,n.kt)("p",null,"The request for the ",(0,n.kt)("inlineCode",{parentName:"p"},"$aggregate")," operation is either a GET request, or a POST\nrequest containing a ",(0,n.kt)("a",{parentName:"p",href:"https://hl7.org/fhir/R4/parameters.html"},"Parameters"),"\nresource. The following parameters are supported:"),(0,n.kt)("ul",null,(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"aggregation [1..*]")," - (string) A FHIRPath expression which is used to\ncalculate a summary value from each grouping. The context is a collection of\nresources of the subject resource type. The expression must return a\n",(0,n.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#materializable-types"},"materializable type")," and also\nbe\nsingular."),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"grouping [0..*]")," - (string) A FHIRPath expression that can be evaluated\nagainst each resource in the data set to determine which groupings it should\nbe counted within. The context is an individual resource of the subject\nresource type. The expression must return a\n",(0,n.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#materializable-types"},"materializable type"),"."),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"filter [0..*]")," - (string) A FHIRPath expression that can be evaluated against\neach resource in the data set to determine whether it is included within the\nresult. The context is an individual resource of the subject resource type.\nThe expression must evaluate to a Boolean value. Multiple filters are combined\nusing AND logic.")),(0,n.kt)("h2",{id:"response"},"Response"),(0,n.kt)("p",null,"The response for the ",(0,n.kt)("inlineCode",{parentName:"p"},"$aggregate")," operation is a\n",(0,n.kt)("a",{parentName:"p",href:"https://hl7.org/fhir/R4/parameters.html"},"Parameters")," resource containing the\nfollowing parameters:"),(0,n.kt)("ul",null,(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"grouping [0..*]")," - The grouped results of the aggregations requested in the\nquery. There will be one grouping for each distinct combination of values\ndetermined by executing the grouping expressions against each of the resources\nwithin the filtered set of subject resources.",(0,n.kt)("ul",{parentName:"li"},(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"label [0..*]")," - (",(0,n.kt)("a",{parentName:"li",href:"https://hl7.org/fhir/R4/datatypes.html#primitive"},"Type"),")\nThe set of descriptive labels that describe this grouping, corresponding to\nthose requested in the query. There will be one label for each grouping\nwithin the query, and the type of each label will correspond to the type\nreturned by the expression of the corresponding grouping. A grouping\nexpression that results in an empty collection will yield a null label,\nwhich is represented within FHIR as the absence of a value."),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"result [1..*]")," - (",(0,n.kt)("a",{parentName:"li",href:"https://hl7.org/fhir/R4/datatypes.html#primitive"},"Type"),")\nThe set of values that resulted from the execution of the aggregations that\nwere requested in the query. There will be one result for each aggregation\nwithin the query, and the type of each result will correspond to the type\nreturned by the expression of the corresponding aggregation."),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"drillDown [0..1]")," - (string) A FHIRPath expression that can be used as a\nfilter to retrieve the set of resources that are members of this grouping.\nThis will be omitted if there were no groupings or filters passed within the\nquery.")))),(0,n.kt)("h2",{id:"examples"},"Examples"),(0,n.kt)("p",null,"Check out example ",(0,n.kt)("inlineCode",{parentName:"p"},"aggregate")," requests in the Postman collection:"),(0,n.kt)("a",{class:"postman-link",href:"https://documenter.getpostman.com/view/634774/UVsQs48s#83ef69d8-0cb7-43c2-9f43-f55ffb3ed940"},(0,n.kt)("img",{src:"https://run.pstmn.io/button.svg",alt:"Run in Postman"})))}h.isMDXComponent=!0},7244:(e,t,r)=>{r.d(t,{Z:()=>a});const a=r.p+"assets/images/aggregate-dark-12701985341956c1762632d482b47c67.svg"},4638:(e,t,r)=>{r.d(t,{Z:()=>a});const a=r.p+"assets/images/aggregate-a35ba364ea611eba016dd3b99978f3e7.svg"}}]);