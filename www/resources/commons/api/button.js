//button.js


function elveos_ajax(url, callback) {
    //var xhr;
    
    var xhr = null;
    
    if (window.XDomainRequest) {
        xhr = new XDomainRequest(); 
    } else if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest(); 
    } else {
        return;
    }
    
    xhr.onreadystatechange = function(){
        if (xhr.readyState == 4) { // Received
           callback(xhr.responseXML);
        } else {
        }
    };
    xhr.open('GET', url, true);                  
    xhr.send(null);
}

function elveos_startGenerateButton(button) {
    featureId = button.getAttribute('data-feature-id');
    
    //var host = 'https://elveos.org'
    //var host = 'http://f2.b219.org:8081'
   var host = 'http://'+ window.location.hostname;    
    elveos_ajax(host + '/rest/features/'+featureId, function(xml) {
    contribution = parseFloat(xml.getElementsByTagName('contribution')[0].firstChild.data);
    button.innerHTML = "" + contribution + " €";
    button.style.display = "inline-block";
    button.style.height = "18px";
    button.style.width = "50px";
    button.style.paddingLeft = "20px";
    button.style.border = "1px solid rgb(0,82,108)";
    button.style.background = "white";
    }
    );
}


(function () {
    var buttons = document.getElementsByClassName('elveos-button');
    var button;
    for(var i = 0; ((button = buttons[i]) != null); i++) {
        
        if(button != undefined  && button.getAttribute('data-generated') != 'true') {   
            button.setAttribute('data-generated', 'true');
            elveos_startGenerateButton(button);
        }
    }  
})();
