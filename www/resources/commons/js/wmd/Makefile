
JSFILES=wmd.js showdown.js

all: wmd.combined.js wmd.combined.min.js

wmd.combined.js: $(JSFILES)
	cat $(JSFILES) > $@

wmd.combined.min.js: $(JSFILES)
	cat $(JSFILES) | python jsmin.py > $@
