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

function elveos_round(value, decimal) {
    return parseFloat((value).toFixed(decimal));
}

function elveos_formatMoney(contribution) {
    if(contribution > 999999) {
        value = contribution / 1000000;
        unit = "M€";
    } else if(contribution > 1999) {
        value = contribution / 1000;
        unit = "k€";
    } else {
        value = contribution
        unit = "€";
    }
    if(value >= 10) {
        textValue = elveos_round(value, 0);
    } else  {
        textValue = elveos_round(value, 1);
    }
    return "" + textValue + " " +unit;
}

function elveos_startGenerateButton(button) {
    var featureId = button.getAttribute('data-feature-id');
    var buttonStyle = button.getAttribute('data-button-style');
    
    //var host = 'https://elveos.org'
    //var host = 'http://f2.b219.org:8081'
    var host = 'http://'+ window.location.hostname;    
    elveos_ajax(host + '/rest/features/'+featureId, function(xml) {
    var contribution = parseFloat(xml.getElementsByTagName('contribution')[0].firstChild.data);
    var progression = parseFloat(xml.getElementsByTagName('feature')[0].getAttribute("progression"));
    var featureState = xml.getElementsByTagName('feature')[0].getAttribute("featureState");
    var offerCount = xml.getElementsByTagName('offers').length;
    button.innerHTML = elveos_formatMoney(contribution);
    button.style.display = "inline-block";
    button.style.textAlign = "center";
    button.style.font= "bold 12px/20px Arial,Sans";
    button.style.color= "black";
    
    
    if(buttonStyle == "short_small") {
        button.style.lineHeight= "20px";
        button.style.height = "20px";
        button.style.width = "50px";
        button.style.paddingLeft = "27px";
        button.style.fontSize = "11px";
    } else if (buttonStyle == "short_medium") {
        button.style.lineHeight= "26px";
        button.style.height = "26px";
        button.style.width = "64px";
        button.style.paddingLeft = "37px";
        button.style.fontSize = "16px";
    } else if (buttonStyle == "short_big") {
        button.style.lineHeight= "34px";
        button.style.height = "34px";
        button.style.width = "83px";
        button.style.paddingLeft = "48px";
        button.style.fontSize = "18px";
    } else if (buttonStyle == "long_small") {
        button.style.lineHeight= "20px";
        button.style.height = "20px";
        button.style.width = "50px";
        button.style.paddingLeft = "74px";
        button.style.fontSize = "11px";
    } else if (buttonStyle == "long_medium") {
        button.style.lineHeight= "26px";
        button.style.height = "26px";
        button.style.width = "65px";
        button.style.paddingLeft = "95px";
        button.style.fontSize = "16px";
    } else if (buttonStyle == "long_big") {
        button.style.lineHeight= "34px";
        button.style.height = "34px";
        button.style.width = "83px";
        button.style.paddingLeft = "127px";
        button.style.fontSize = "18px";
    }
    
    var progress = parseInt(progression);
    progress = progress - progress%10;
   
    if(contribution == 0 || featureState == "PENDING") {
        progress = "empty";
    } else if (featureState == "FINISHED") {
        progress = "success";
    } else if (progress > 100) {
        progress = 100;
    } 
    button.style.background = "url(\""+host+"/resources/commons/api/button_"+buttonStyle+"_"+progress+".png\") no-repeat scroll 0 0 transparent";
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
