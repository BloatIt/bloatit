
$("#software_chooser_failback").hide();


function Dropdown(referenceElement, targetInputElement) {

    this.targetInputElement = targetInputElement;
    this.referenceElement = referenceElement;
    this.isShown = false;
    this.entryList = ['Tryton', 'Inkscape', 'Genetic Invasion', 'Libreoffice', 'Piwik', 'Firefox', 'Shotwell', 'Gimp', 'Mumble', 'Qt Linguist', 'Silverpeas', 'Eye Of Gnome', 'Perroquet', 'VLC'];

    this.keyList = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14];
    this.maxResult = 5;
    

    this.init = function() {
        This = this
        this.targetInputElement.bind('focusin', function() {This.focusin();});
        this.targetInputElement.bind('focusout',  function() {This.focusout();});
        this.targetInputElement.bind('keypress',  function(event) {This.keypress(event);});
        this.targetInputElement.bind('input',  function() {This.change();});
        $("form").bind('keypress',  function(event) {if(event.keyCode == 13 && targetInputElement.is(":focus") ) {return false;}});

        this.maxResult = 5;
    }

    this.show = function() {
        if(this.isShown) {
            return;
        }
    
        this.isShown = true;
        this.panel = $(document.createElement('div'));
        this.panel.addClass("dropdown_panel");
        //this.panel.html("coucou");
        
        this.listContainer = $(document.createElement('div'));
        this.listContainer.addClass("list_container");
        this.panel.append(this.listContainer);
        
        this.resultCount = $(document.createElement('div'));
        this.resultCount.addClass("result_count");
        this.panel.append(this.resultCount);

        
        referenceElement.append(this.panel)
        this.updateSearch();
    }
    
    
    this.hide = function() {
        if(!this.isShown) {
            return;
        }

        this.isShown = false;
        this.panel.remove();
    }
    
    this.focusin = function() {

        if(this.targetInputElement.val().length > 0) {
            this.show();
        }
    }
    
    this.focusout = function() {
        //this.hide();
    }
    
    this.keypress = function(event) {
        
        console.log("key event"+ event.keyCode);
        if(event.keyCode == 27) {
            this.hide();
            return false;
        }
        if(event.keyCode == 13 && this.isShown) {
            this.chooseSelection();
            return false;
        }
        
        if(event.keyCode == 40) {
            if(this.isShown) {
                this.selectNext();
            } else {
                this.show();
            }
        }
        
        if(event.keyCode == 38) {
            if(this.isShown) {
                this.selectPrevious();
            }
        }
        
        
        if(event.which != 0) {
            this.show();
        }
    }
    
    this.change = function() {
        this.updateSearch();
    }
    
    this.updateSearch = function() {
        userInput = this.targetInputElement.val();
        console.log("update search: "+ userInput);

        this.emptyList()

        if(userInput.length == 0) {
            for(var i = 0; i < this.entryList.length; i++) {
                var entry = this.entryList[i];
                this.addLine(this.keyList[i],entry, '', '');
            }
        } else {
            regexp = new RegExp("^(.*)("+userInput+")(.*)$","i");
            
            for(var i = 0; i < this.entryList.length; i++) {
                var entry = this.entryList[i];
                var match = regexp.exec(entry);

                if(match != null) {
                    this.addLine(i, match[1], match[2], match[3]  )
                }
            }
        }
        
        if(this.activeList.length > 0) {
            this.select(0);
            
            var extraResult = this.activeList.length - this.maxResult; 
            
            if(extraResult <= 0 ) {
                this.resultCount.html("no additional results");
            } else if (extraResult == 1) {
                this.resultCount.html("1 additionnal results");            
            } else {
                this.resultCount.html(extraResult+" additionnal results");
            }
        } else {
            this.resultCount.html("no results");
        }
    
    }
    
    
    this.emptyList = function() {
        this.listContainer.empty();
        this.activeList = new Array();
        this.selection = -1;
    }
    
    
    this.addLine = function(index, preMatch, matchString, postMatch) {

        this.activeList.push(index);

        if(this.activeList.length <= this.maxResult) {
            line = $(document.createElement('p'));
            line.html(preMatch+'<b>'+matchString+'</b>'+postMatch);
            this.listContainer.append(line);
        }
        
    }
    
    this.select = function(index) {
        if(index >= this.activeList.length || index >= this.maxResult || index < 0) {
            return;
        }
    
        this.selection = index;
    
        var children = this.listContainer.children()
        
        for(var i = 0; i < children.length; i++) {
            var child = $(children[i]);
            console.log(child);
            if(i == index) {
                if(!child.hasClass('entry_selected')) {
                    child.addClass('entry_selected');
                }
            } else {
               if(child.hasClass('entry_selected')) {
                    child.removeClass('entry_selected');
                }
            }
        }
    }
    
    this.selectNext = function() {
        this.select(this.selection+1);
    }
    
    this.selectPrevious = function() {
        this.select(this.selection-1);
    }

    
    this.chooseSelection = function() {
        if(this.selection == -1) {
            return;
        }
        
        this.targetInputElement.val(this.entryList[this.activeList[this.selection]]);        
        this.valueChange(this.keyList[this.activeList[this.selection]]);
        
        this.hide();
        
        
    }

    this.valueChange = function() {
        //To overwrite
    }

    
    this.init()
}

new Dropdown($("#software_chooser_block_id"), $("#software_chooser_search_id"));
