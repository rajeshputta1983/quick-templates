Quick-Templates is a templating engine/framework with following feature set,

-	Support for wide variety of input data, i.e., XML, JSON, CSV, Excel, Java Objects and is extendable with other input formats
-	Pick template based on the configured rules
-	Rules can be expressions of XML, JSON, CSV, Excel, any input sources (below are few examples/formats of expressions)
o	#someXml.inventory.book[2]@year (XML based expression)
o	#someJson.person.address[1].location.substring(0,2)
o	#customObject.getNames()[0]=='Quick'
-	Pre-Compiled and Cached templates to offer better performance while serving the request
-	Parallel Pre-compilation capability to pre-process all the configured templates and cache quickly
-	Support for both Template Engine and Rule Engine, can be used independently based on application needs
-	If-elseif-else constructs, switch-case constructs, for-each loops, Expression Tags and nested constructs can be used in the *.qt (template files) 
-	Boolean expressions
-	Dynamic Method Calls on XML, JSON, CSV, Excel, Java Objects data
-	Support for wide variety of output formats, i.e., XML, JSON, HTML, Plain Text, Source Code, etc and is extendable with other output formats

