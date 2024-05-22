"use strict";(self.webpackChunkpathling_site=self.webpackChunkpathling_site||[]).push([[828],{3905:(t,e,a)=>{a.d(e,{Zo:()=>m,kt:()=>k});var n=a(7294);function r(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}function l(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,n)}return a}function i(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?l(Object(a),!0).forEach((function(e){r(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):l(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}function o(t,e){if(null==t)return{};var a,n,r=function(t,e){if(null==t)return{};var a,n,r={},l=Object.keys(t);for(n=0;n<l.length;n++)a=l[n],e.indexOf(a)>=0||(r[a]=t[a]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(n=0;n<l.length;n++)a=l[n],e.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(t,a)&&(r[a]=t[a])}return r}var p=n.createContext({}),s=function(t){var e=n.useContext(p),a=e;return t&&(a="function"==typeof t?t(e):i(i({},e),t)),a},m=function(t){var e=s(t.components);return n.createElement(p.Provider,{value:e},t.children)},u="mdxType",d={inlineCode:"code",wrapper:function(t){var e=t.children;return n.createElement(n.Fragment,{},e)}},h=n.forwardRef((function(t,e){var a=t.components,r=t.mdxType,l=t.originalType,p=t.parentName,m=o(t,["components","mdxType","originalType","parentName"]),u=s(a),h=r,k=u["".concat(p,".").concat(h)]||u[h]||d[h]||l;return a?n.createElement(k,i(i({ref:e},m),{},{components:a})):n.createElement(k,i({ref:e},m))}));function k(t,e){var a=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=a.length,i=new Array(l);i[0]=h;var o={};for(var p in e)hasOwnProperty.call(e,p)&&(o[p]=e[p]);o.originalType=t,o[u]="string"==typeof t?t:r,i[1]=o;for(var s=2;s<l;s++)i[s]=a[s];return n.createElement.apply(null,i)}return n.createElement.apply(null,a)}h.displayName="MDXCreateElement"},8957:(t,e,a)=>{a.r(e),a.d(e,{assets:()=>p,contentTitle:()=>i,default:()=>d,frontMatter:()=>l,metadata:()=>o,toc:()=>s});var n=a(7462),r=(a(7294),a(3905));const l={sidebar_position:3},i="Operators",o={unversionedId:"fhirpath/operators",id:"fhirpath/operators",title:"Operators",description:"Operators are special symbols or keywords that take a left and right operand,",source:"@site/docs/fhirpath/operators.md",sourceDirName:"fhirpath",slug:"/fhirpath/operators",permalink:"/docs/fhirpath/operators",draft:!1,editUrl:"https://github.com/aehrc/pathling/tree/main/site/docs/fhirpath/operators.md",tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3},sidebar:"fhirpath",previous:{title:"Data types",permalink:"/docs/fhirpath/data-types"},next:{title:"Functions",permalink:"/docs/fhirpath/functions"}},p={},s=[{value:"Comparison",id:"comparison",level:2},{value:"Equality",id:"equality",level:2},{value:"Math",id:"math",level:2},{value:"Date/time arithmetic",id:"datetime-arithmetic",level:2},{value:"Boolean logic",id:"boolean-logic",level:2},{value:"Membership",id:"membership",level:2},{value:"combine",id:"combine",level:2}],m={toc:s},u="wrapper";function d(t){let{components:e,...a}=t;return(0,r.kt)(u,(0,n.Z)({},m,a,{components:e,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"operators"},"Operators"),(0,r.kt)("p",null,"Operators are special symbols or keywords that take a left and right operand,\nreturning some sort of result."),(0,r.kt)("h2",{id:"comparison"},"Comparison"),(0,r.kt)("p",null,"The following comparison operators are supported:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"<=")," - Less than or equal to"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"<")," - Less than"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},">")," - Greater than"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},">=")," - Greater than or equal to")),(0,r.kt)("p",null,"Both operands must be singular, the table below shows the valid types and their\ncombinations."),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null}),(0,r.kt)("th",{parentName:"tr",align:null},"Boolean"),(0,r.kt)("th",{parentName:"tr",align:null},"String"),(0,r.kt)("th",{parentName:"tr",align:null},"Integer"),(0,r.kt)("th",{parentName:"tr",align:null},"Decimal"),(0,r.kt)("th",{parentName:"tr",align:null},"Date"),(0,r.kt)("th",{parentName:"tr",align:null},"DateTime"),(0,r.kt)("th",{parentName:"tr",align:null},"Time"),(0,r.kt)("th",{parentName:"tr",align:null},"Quantity"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Boolean"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"String"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Integer"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Decimal"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Date"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"DateTime"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Time"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true"),(0,r.kt)("td",{parentName:"tr",align:null},"false")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Quantity"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"false"),(0,r.kt)("td",{parentName:"tr",align:null},"true",(0,r.kt)("sup",null,"*"))))),(0,r.kt)("p",null,"If one or both of the operands is an empty collection, the operator will return\nan empty collection."),(0,r.kt)("p",null,"String ordering is strictly lexical and is based on the Unicode value of the\nindividual characters."),(0,r.kt)("p",null,"All comparison operators return a ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#boolean"},"Boolean")," value."),(0,r.kt)("admonition",{type:"caution"},(0,r.kt)("p",{parentName:"admonition"},"The comparability of units within Quantities is defined\nwithin ",(0,r.kt)("a",{href:"https://unitsofmeasure.org/ucum"},"UCUM"),".\nYou can use the ",(0,r.kt)("a",{href:"https://ucum.nlm.nih.gov/ucum-lhc/demo.html"},"NLM Converter Tool")," to\ncheck whether your units are comparable to each other.")),(0,r.kt)("p",null,"See also: ",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#comparison"},"Comparison")),(0,r.kt)("h2",{id:"equality"},"Equality"),(0,r.kt)("p",null,"The ",(0,r.kt)("inlineCode",{parentName:"p"},"=")," operator returns ",(0,r.kt)("inlineCode",{parentName:"p"},"true")," if the left operand is equal to the right\noperand, and a ",(0,r.kt)("inlineCode",{parentName:"p"},"false")," otherwise. The ",(0,r.kt)("inlineCode",{parentName:"p"},"!=")," is the inverse of the ",(0,r.kt)("inlineCode",{parentName:"p"},"=")," operator."),(0,r.kt)("p",null,"Both operands must be singular. The valid types and their combinations is the\nsame as for the ",(0,r.kt)("a",{parentName:"p",href:"#comparison"},"Comparison operators"),". In addition to this,\n",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#coding"},"Coding")," types can\nbe compared using the equality operators."),(0,r.kt)("p",null,"If one or both of the operands is an empty collection, the operator will return\nan empty collection."),(0,r.kt)("p",null,"If the operands are Quantity values and are not comparable, an empty collection\nwill be returned."),(0,r.kt)("admonition",{type:"caution"},(0,r.kt)("p",{parentName:"admonition"},"The comparability of units within Quantities is defined\nwithin ",(0,r.kt)("a",{href:"https://unitsofmeasure.org/ucum"},"UCUM"),".\nYou can use the ",(0,r.kt)("a",{href:"https://ucum.nlm.nih.gov/ucum-lhc/demo.html"},"NLM Converter Tool")," to\ncheck whether your units are comparable to each other.")),(0,r.kt)("p",null,"See also: ",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#equality"},"Equality")),(0,r.kt)("h2",{id:"math"},"Math"),(0,r.kt)("p",null,"The following math operators are supported:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"+")," - Addition"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"-")," - Subtraction"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"*")," - Multiplication"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"/")," - Division"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"mod")," - Modulus")),(0,r.kt)("p",null,"Math operators support ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#integer"},"Integer"),",\n",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#decimal"},"Decimal"),"\nand ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#quantity"},"Quantity")," operands. The modulus\noperator is not supported for Quantity types."),(0,r.kt)("p",null,"Both operands must be singular."),(0,r.kt)("p",null,"If one or both of the operands is an empty collection, the operator will return\nan empty collection."),(0,r.kt)("p",null,"Integer and Decimal types can be mixed, while Quantity types can only be used\nwith other Quantity types. "),(0,r.kt)("p",null,"For Integer and Decimal, ",(0,r.kt)("inlineCode",{parentName:"p"},"+"),", ",(0,r.kt)("inlineCode",{parentName:"p"},"-")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"*")," return the same type as the left\noperand, ",(0,r.kt)("inlineCode",{parentName:"p"},"/")," returns ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#decimal"},"Decimal")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"mod"),"\nreturns ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#integer"},"Integer"),"."),(0,r.kt)("p",null,"For Quantity types, math operators return a new Quantity with the canonical unit\ncommon to both operands. If the units are not comparable, an empty collection is\nreturned."),(0,r.kt)("p",null,"See also: ",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#math-2"},"Math")),(0,r.kt)("h2",{id:"datetime-arithmetic"},"Date/time arithmetic"),(0,r.kt)("p",null,"The following operators are supported for date arithmetic:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"+")," - Add a duration to a ",(0,r.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#date"},"Date")," or\n",(0,r.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#datetime"},"DateTime")),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"-")," - Subtract a duration from a ",(0,r.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#date"},"Date")," or\n",(0,r.kt)("a",{parentName:"li",href:"/docs/fhirpath/data-types#datetime"},"DateTime"))),(0,r.kt)("p",null,"Date arithmetic always has a ",(0,r.kt)("inlineCode",{parentName:"p"},"DateTime")," or ",(0,r.kt)("inlineCode",{parentName:"p"},"Date")," on the left-hand side, and a\nduration on the right-hand side. The duration operand is a\n",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#quantity"},"calendar duration literal"),". The use of UCUM units\nis not supported with these operators."),(0,r.kt)("p",null,"The ",(0,r.kt)("inlineCode",{parentName:"p"},"Date")," or ",(0,r.kt)("inlineCode",{parentName:"p"},"DateTime")," operand must be singular. If it is an empty collection,\nthe operator will return an empty collection."),(0,r.kt)("admonition",{type:"note"},(0,r.kt)("p",{parentName:"admonition"},"The use of arithmetic with the ",(0,r.kt)("a",{href:"/docs/fhirpath/data-types#time"},"Time"),"\ntype is not supported.")),(0,r.kt)("admonition",{type:"caution"},(0,r.kt)("p",{parentName:"admonition"},"Arithmetic\ninvolving ",(0,r.kt)("a",{href:"https://hl7.org/fhir/datatypes.html#instant"},"instant"),"\nvalues is limited to a precision of seconds.")),(0,r.kt)("p",null,"See also: ",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#datetime-arithmetic"},"Date/Time Arithmetic")),(0,r.kt)("h2",{id:"boolean-logic"},"Boolean logic"),(0,r.kt)("p",null,"The following Boolean operations are supported:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"and")),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"or")),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"xor")," - Exclusive OR"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"implies")," - Material implication")),(0,r.kt)("p",null,"Both operands to a Boolean operator must be singular\n",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#boolean"},"Boolean")," values."),(0,r.kt)("p",null,"All Boolean operators return a ",(0,r.kt)("a",{parentName:"p",href:"/docs/fhirpath/data-types#boolean"},"Boolean")," value."),(0,r.kt)("p",null,"See also:\n",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#boolean-logic"},"Boolean logic")),(0,r.kt)("h2",{id:"membership"},"Membership"),(0,r.kt)("p",null,"The following membership operators are supported:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"in")," (",(0,r.kt)("inlineCode",{parentName:"li"},"[element] in [collection]"),")"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"contains")," (",(0,r.kt)("inlineCode",{parentName:"li"},"[collection] contains [element]"),")")),(0,r.kt)("p",null,"If the element operand is a collection with a single item, the operator\nreturns ",(0,r.kt)("inlineCode",{parentName:"p"},"true")," if the item is in the collection using ",(0,r.kt)("a",{parentName:"p",href:"#equality"},"equality"),"\nsemantics."),(0,r.kt)("p",null,"If the element is empty, the result is empty. If the collection is empty, the\nresult is ",(0,r.kt)("inlineCode",{parentName:"p"},"false"),". If the element has multiple items, an error is returned."),(0,r.kt)("p",null,"See also:\n",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#collections-2"},"Collections")),(0,r.kt)("h2",{id:"combine"},"combine"),(0,r.kt)("p",null,"The ",(0,r.kt)("inlineCode",{parentName:"p"},"combine")," operator merges the left and right operands into a single\ncollection, preserving duplicates. The result is not ordered."),(0,r.kt)("p",null,"The two operands provided to the ",(0,r.kt)("inlineCode",{parentName:"p"},"combine")," operator must share the same type."),(0,r.kt)("p",null,"This implementation has the same semantics as\nthe ",(0,r.kt)("a",{parentName:"p",href:"https://hl7.org/fhirpath/#combineother-collection-collection"},"combine function"),"\nwithin the FHIRPath specification, but is implemented as an operator."))}d.isMDXComponent=!0}}]);