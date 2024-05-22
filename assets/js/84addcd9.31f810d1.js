"use strict";(self.webpackChunkpathling_site=self.webpackChunkpathling_site||[]).push([[189],{3905:(e,t,n)=>{n.d(t,{Zo:()=>c,kt:()=>m});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function o(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var s=a.createContext({}),p=function(e){var t=a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},c=function(e){var t=p(e.components);return a.createElement(s.Provider,{value:t},e.children)},u="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},h=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,i=e.originalType,s=e.parentName,c=o(e,["components","mdxType","originalType","parentName"]),u=p(n),h=r,m=u["".concat(s,".").concat(h)]||u[h]||d[h]||i;return n?a.createElement(m,l(l({ref:t},c),{},{components:n})):a.createElement(m,l({ref:t},c))}));function m(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var i=n.length,l=new Array(i);l[0]=h;var o={};for(var s in t)hasOwnProperty.call(t,s)&&(o[s]=t[s]);o.originalType=e,o[u]="string"==typeof e?e:r,l[1]=o;for(var p=2;p<i;p++)l[p]=n[p];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}h.displayName="MDXCreateElement"},5581:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>l,default:()=>d,frontMatter:()=>i,metadata:()=>o,toc:()=>p});var a=n(7462),r=(n(7294),n(3905));const i={sidebar_position:1,title:"Installation",sidebar_label:"Installation",description:"Instructions for installing the Pathling libraries for Python, R, Scala, and Java."},l=void 0,o={unversionedId:"libraries/installation/index",id:"libraries/installation/index",title:"Installation",description:"Instructions for installing the Pathling libraries for Python, R, Scala, and Java.",source:"@site/docs/libraries/installation/index.md",sourceDirName:"libraries/installation",slug:"/libraries/installation/",permalink:"/docs/libraries/installation/",draft:!1,editUrl:"https://github.com/aehrc/pathling/tree/main/site/docs/libraries/installation/index.md",tags:[],version:"current",sidebarPosition:1,frontMatter:{sidebar_position:1,title:"Installation",sidebar_label:"Installation",description:"Instructions for installing the Pathling libraries for Python, R, Scala, and Java."},sidebar:"libraries",previous:{title:"Introduction",permalink:"/docs/libraries/"},next:{title:"Windows installation",permalink:"/docs/libraries/installation/windows"}},s={},p=[{value:"Python",id:"python",level:3},{value:"R",id:"r",level:3},{value:"Scala",id:"scala",level:3},{value:"Java",id:"java",level:3},{value:"Java Virtual Machine",id:"java-virtual-machine",level:3}],c={toc:p},u="wrapper";function d(e){let{components:t,...n}=e;return(0,r.kt)(u,(0,a.Z)({},c,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h3",{id:"python"},"Python"),(0,r.kt)("p",null,"Prerequisites:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"Python 3.8+ with pip")),(0,r.kt)("p",null,"To install, run this command:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre"},"pip install pathling  \n")),(0,r.kt)("h3",{id:"r"},"R"),(0,r.kt)("p",null,"Prerequisites:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"R >= 3.5.0 (tested with 4.3.1)")),(0,r.kt)("p",null,"To install, run these commands:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-r"},"# Install the `pathling` package.\ninstall.packages('pathling')\n\n# Install the Spark version required by Pathling.\npathling::pathling_install_spark()\n")),(0,r.kt)("h3",{id:"scala"},"Scala"),(0,r.kt)("p",null,"To add the Pathling library to your project, add the following to\nyour ",(0,r.kt)("a",{parentName:"p",href:"https://www.scala-sbt.org/"},"SBT")," configuration:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-scala"},'libraryDependencies += "au.csiro.pathling" % "library-runtime" % "[version]"\n')),(0,r.kt)("h3",{id:"java"},"Java"),(0,r.kt)("p",null,"To add the Pathling library to your project, add the following to\nyour ",(0,r.kt)("inlineCode",{parentName:"p"},"pom.xml"),":"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-xml"},"\n<dependency>\n    <groupId>au.csiro.pathling</groupId>\n    <artifactId>library-runtime</artifactId>\n    <version>[version]</version>\n</dependency>\n")),(0,r.kt)("h3",{id:"java-virtual-machine"},"Java Virtual Machine"),(0,r.kt)("p",null,"All variants of the Pathling library require version 17 of a Java Virtual\nMachine (JVM) to be installed. We recommend using Azul OpenJDK, you can download\ninstallers for all major operating systems at\nthe ",(0,r.kt)("a",{parentName:"p",href:"https://www.azul.com/downloads/?version=java-17-lts#zulu"},"Azul OpenJDK"),"\nwebsite."),(0,r.kt)("p",null,"Ensure that the ",(0,r.kt)("inlineCode",{parentName:"p"},"JAVA_HOME")," environment variable is set to the location of the\ninstallation of Java 17."))}d.isMDXComponent=!0}}]);