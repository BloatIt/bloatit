$("#software_chooser_fallback").hide();
$("#software_chooser_search_id").show();
$(".new_software_checkbox_block").show();


Array.prototype.contains = function (element) {
for (var i = 0; i < this.length; i++) {
    if (this[i] == element) {
        return true;
    }
    }
    return false;
}


function Dropdown(referenceElement, targetInputElement, entryList, keyList) {

    this.targetInputElement = targetInputElement;
    this.referenceElement = referenceElement;
    this.isShown = false;
    this.entryList = entryList;

    this.keyList = keyList;
    this.maxResult = 5;
    

    this.init = function() {
        This = this
        this.targetInputElement.bind('focusin', function() {This.focusin();});
        this.targetInputElement.bind('focusout',  function() {This.focusout();});
        this.targetInputElement.bind('keypress',  function(event) {This.keypress(event);});
        this.targetInputElement.bind('input',  function() {This.change();});
        $("form").bind('keypress',  function(event) {if(event.keyCode == 13) {return false;}});

        this.lastChoose = this.getFieldValue();
        this.maxResult = 5;
        this.canceling = false;
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

        this.softwareAdder = $(document.createElement('div'));
        this.softwareAdder.addClass("software_adder");
        this.panel.append(this.softwareAdder);

        
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

        if(this.getFieldValue().length > 0) {
            this.show();
        }
    }
    
    this.focusout = function() {
    
        if(this.hasExactMatch) {
            this.chooseSelection();
        } else {
            var that = this;
            
            window.setTimeout(function() {
            that.cancel()
            }, 150);
        }
        
        
    }
    

    
    
    this.keypress = function(event) {
        
        console.log("key event"+ event.keyCode);
        //ESCAPE
        if(event.keyCode == 27) {
            
            var that = this;
            
            window.setTimeout(function() {
            that.cancel()
            }, 150);
            return true;
        }
        
        //ENTER
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
        
        //Tab
        if(event.keyCode == 9) {
            if(this.isShown && this.selection != "new") {
                this.chooseSelection();
                
            } else {
                return true     
            }
        }
        
        
        if(event.which != 0) {
            this.show();
        }
    }
    
    this.change = function() {
        console.log("change");
        this.updateSearch();
    }
    
    this.getFieldValue = function() {
        return $.trim(this.targetInputElement.val());
    }
    
    this.updateSearch = function() {
        userInput = this.getFieldValue();
        console.log("update search: "+ userInput);
        this.hasExactMatch = false;
        this.emptyList()

        if(userInput.length == 0) {
            for(var i = 0; i < this.entryList.length; i++) {
                var entry = this.entryList[i];
                this.addLine(i,entry, '', '');
            }
        } else {
            regexp1 = new RegExp("^()("+userInput+")(.*)$","i");
            this.addLinesWithRegExp(regexp1)
            
            
            regexp2 = new RegExp("^(.*)("+userInput+")(.*)$","i");
            this.addLinesWithRegExp(regexp2)

        }
        
        
        tomComment = "";
        if(userInput.length < 3) {
            tomComment = '<div class="lenght_comment">The sofware name must have at least 3 characters</div>';
        } else if(userInput[0].toLowerCase() == userInput[0]) {
             tomComment = '<div class="tom_comment">A sofware name is often prettier with a capital</div>';
        }
        this.softwareAdder.html("<p>Add <strong>"+userInput+"</strong> to Elveos</p>"+tomComment);
        
        adder = $(this.softwareAdder.children()[0]);
        adder.bind('click', function() {
                This.select('new');
                if(userInput.length > 0  && !This.hasExactMatch) {
                    This.chooseSelection();
                }
                return false;
            });

        adder.bind('mouseover', function() {
            This.select('new');
            return false;
        });
        
        
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
            this.select("new");
        }
        
        
        if(userInput.length < 3 || this.hasExactMatch && !$(this.softwareAdder.children()[0]).hasClass('entry_disabled')) {
               $(this.softwareAdder.children()[0]).addClass('entry_disabled');
        } else if(!this.hasExactMatch && $(this.softwareAdder.children()[0]).hasClass('entry_disabled')) {
                $(this.softwareAdder.children()[0]).removeClass('entry_disabled');
        }
        
        
        
    
    }
    
    
    this.emptyList = function() {
        this.listContainer.empty();
        this.activeList = new Array();
        this.selection = -1;
    }
    
    
    this.addLinesWithRegExp = function(regexp) {
        for(var i = 0; i < this.entryList.length; i++) {
                var entry = this.entryList[i];
                var match = regexp.exec(entry);

                if(match != null) {
                    if(match[1].length == 0 && match[3].length == 0) {
                        this.hasExactMatch = true;
                    }
                
                    this.addLine(i, match[1], match[2], match[3]  )
                }
            }
        }
    
    this.addLine = function(index, preMatch, matchString, postMatch) {

        if(this.activeList.contains(index)) {
            return;
        }

        this.activeList.push(index);
        var currentIndex = this.activeList.length-1

        if(this.activeList.length <= this.maxResult) {
            line = $(document.createElement('p'));
            line.html(preMatch+'<strong>'+matchString+'</strong>'+postMatch);

            var This = this;
            
            line.bind('click', function() {
                console.log('click');
                This.select(currentIndex);
                This.chooseSelection();
                return false;
            });

            line.bind('mouseover', function() {
                This.select(currentIndex);
                return false;
            });
            
            this.listContainer.append(line);
        }
        
    }
    
    this.select = function(index) {
        
        if(index == "new") {
            if(userInput.length < 3 || this.hasExactMatch) {
                return;
            }
            
            if(!$(this.softwareAdder.children()[0]).hasClass('entry_selected')) {
                    $(this.softwareAdder.children()[0]).addClass('entry_selected');
            }
            
        
            var children = this.listContainer.children()
            for(var i = 0; i < children.length; i++) {
                var child = $(children[i]);
                if(child.hasClass('entry_selected')) {
                    child.removeClass('entry_selected');
                }
            }
            
            this.selection = 'new';
        
        } else {
    
            if(index >= this.activeList.length || index >= this.maxResult || index < 0) {
                return;
            }
        
            this.selection = index;
        
            if($(this.softwareAdder.children()[0]).hasClass('entry_selected')) {
                    $(this.softwareAdder.children()[0]).removeClass('entry_selected');
            }
            
        
            var children = this.listContainer.children()
            for(var i = 0; i < children.length; i++) {
                var child = $(children[i]);
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
       
    }
    
    this.selectNext = function() {
        if(this.selection != 'new') {
            if(this.selection+1  >= this.maxResult || this.selection+1 >= this.activeList.length) {
                this.select("new");
            } else {
                this.select(this.selection+1);            
            }
        }

    }
    
    this.selectPrevious = function() {
        if(this.selection == 'new') {
            if(this.activeList.length  > this.maxResult) {
                this.select(this.maxResult -1)
            } else {
                this.select(this.activeList.length -1)
            }
        } else {
            this.select(this.selection-1);
        }
    }

    
    this.chooseSelection = function() {
        console.log("chooseSelection");
        if(this.selection == -1) {
            return;
        }
        
        if(this.selection == 'new') {
            this.askCreate(this.getFieldValue());
            this.lastChoose = this.getFieldValue();
            this.hasExactMatch = true;
            this.hide();    
        } else {
            this.targetInputElement.val(this.entryList[this.activeList[this.selection]]);        
            this.valueChange(this.keyList[this.activeList[this.selection]]);
            this.lastChoose = this.entryList[this.activeList[this.selection]];
            this.hasExactMatch = true;
            this.hide();
        }
        
        
    }
    
    this.set = function(value) {
        this.lastChoose = value;
        this.targetInputElement.val(value);
    }
    
    this.cancel = function() {
        console.log("cancel");
        if(!this.hasExactMatch) {
            console.log("no match, revert to "+ this.lastChoose);
            this.targetInputElement.val(this.lastChoose);
        }
        
        this.hide();
    }

    this.valueChange = function(value) {
        //To overwrite
    }
    
    this.askCreate = function(newName) {
        //To overwrite
    }

    
    this.init()
}

var softwareNameList = ${software_name_list};
var softwareIdList = ${software_id_list};


if($("#software_chooser_create").val().length == 0) {
    $("#software_chooser_create").val('--invalid--');
}

var dropdown = new Dropdown($("#software_chooser_block_id"), $("#software_chooser_search_id"), softwareNameList,softwareIdList );

dropdown.valueChange = function(value) {
    $("#software_chooser_fallback").val(value)
    $("#software_chooser_create").val("")
}

dropdown.askCreate = function(newName) {
    $("#software_chooser_create").val(newName)
     $("#software_chooser_fallback").val("")
}


var lastChoosenValue
var lastNewSoftwareValue
$("#software_chooser_checkbox_id").click (function () {
    var thisCheck = $(this);
    if (thisCheck.is (':checked'))
    {
        dropdown.cancel()
        $("#software_chooser_search_id").hide();

        lastChoosenValue = $("#software_chooser_fallback").val()
        lastNewSoftwareValue = $("#software_chooser_create").val()
        $("#software_chooser_fallback").val("")
        $("#software_chooser_create").val("")
           
    } else {
        $("#software_chooser_fallback").val(lastChoosenValue)
        $("#software_chooser_create").val(lastNewSoftwareValue)
        $("#software_chooser_search_id").show();
    }
});

if($("#software_chooser_create").val().length > 0 && $("#software_chooser_create").val() != '--invalid--') {
    dropdown.set($("#software_chooser_create").val())
} else {
    var val = $("#software_chooser_fallback").val()
    if(val.length > 0) {
        for(var i = 0; i< softwareIdList.length; i++) {
            if(softwareIdList[i] == val) {
                dropdown.set(softwareNameList[i]);
                break;
            }
        }
    
    }
}

if($("#software_chooser_checkbox_id").is (':checked'))
{
    dropdown.cancel()
    $("#software_chooser_search_id").hide();

    lastChoosenValue = $("#software_chooser_fallback").val()
    lastNewSoftwareValue = $("#software_chooser_create").val()
    $("#software_chooser_fallback").val("")
    $("#software_chooser_create").val("")
       
}


