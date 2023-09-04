/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

// @ts-check

/** @type {import("@docusaurus/plugin-content-docs").SidebarsConfig} */
const sidebars = {
  libraries: [{ type: "autogenerated", dirName: "libraries" },
    {
      type: "link",
      label: "Python API docs",
      href: "pathname:///docs/python/pathling.html"
    },
    {
      type: "link",
      label: "R API docs",
      href: "pathname:///docs/r/index.html"
    },
    {
      type: "link",
      label: "Scala API docs",
      href: "pathname:///docs/scala/au/csiro/pathling/index.html"
    },
    {
      type: "link",
      label: "Java API docs",
      href: "pathname:///docs/java/index.html"
    }
  ],
  server: [{ type: "autogenerated", dirName: "server" }],
  fhirpath: [{ type: "autogenerated", dirName: "fhirpath" }]
};

module.exports = sidebars;
