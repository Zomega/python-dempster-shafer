Dempster-Shafer python implementation
==============================

Origins
---------

This code is ported from (and for the time being includes almost verbatim) a Java library written Thomas Reineking in the fall of 2008. The code is hosted on sourceforge under the GPL.

Goals
------
The eventual purpose is to provide the functionalities needed to help with various ROSStacks (e.g. an open source implementation of the 2005 topological mapping work of Huang and Beevers.
* Provide a pythonic port of existing Demster-Shafer code.
* Avoid depending on any external libraries where practical.
* Keep the interface as simple and general as possible.
* If relevant (i.e. python code slow) provide a cython version with proper bindings.

License(s) and Warranty
-----------------------------

Copyright 2013 Will Oursler

Licensed under LGPL3 (the "License").

You may not use this work except in compliance with the License. You may obtain a copy of the relevant License at http://opensource.org/licenses/lgpl-3.0.html

Unless required by applicable law or agreed to in writing, software distributed under any and all of the Licenses is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
