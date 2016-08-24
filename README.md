# uberfire-eclipse

This project is a proof-of-concept for hosting [Uberfire](http://www.uberfireframework.org/docs)
[Workbench Editors](http://www.uberfireframework.org/docs/overview/editor.html) and 
[Screens](http://www.uberfireframework.org/docs/overview/screen.html) in the Eclipse workbench IDE.

Currently the project contains an Uberfire web application that defines a single
[Perspective](http://www.uberfireframework.org/docs/overview/perspective.html)
which occupies the entire browser page with no other decorations or margins.

The web app also defines a Workbench Editor for a text
[ResourceType](http://www.uberfireframework.org/docs/security/resourceActions.html)
with a "uf" extension - essentially a simple text editor that uses a
[GWT TextArea](http://www.tutorialspoint.com/gwt/gwt_textarea_widget.htm) widget as the implementation.
