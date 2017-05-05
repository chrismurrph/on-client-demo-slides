## jobim-intro

Started off as an example of how to write and test
[jobim](https://github.com/MysteryMachine/jobim-intro) applications.
Used as a basic template for writing slides for om-next-client-demo.

To run it, on your command line, you can call `rlwrap lein figwheel test` to
spin up a Figwheel server that will auto-reload any changes made and
run tests for you. To obtain a minified Javascript file ready to be
included anywhere you want, call `lein cljsbuild once min` instead.

In your browser visit the default figwheel port:
http://localhost:3449/