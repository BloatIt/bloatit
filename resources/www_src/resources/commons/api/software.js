//button.js

var elveos_hostname="https://elveos.org"
//var elveos_hostname="http://127.0.0.1"

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

function elveos_getCss() {
    return "\
<style> \
.elveos-feature {\
    border: 1px solid rgb(127,127,127);\
    border-radius: 10px;\
    padding: 0px;\
    margin: 10px;\
    background: rgb(242,242,242);\
    vertical-align: top;\
    font-family: Arial,Sans;\
    color: rgb(51,51,51);\
    max-width: 900px;\
}\
\
.elveos-feature-title {\
    margin: 0px;\
    margin-top: 5px;\
    margin-bottom: 15px;\
}\
\
.elveos-progress-bar-block {\
    display: inline-block;\
    margin-left: 0px;\
    margin-right: 0px;\
    width: 100%;\
}\
\
.elveos-progress-bar-background, .elveos-progress-bar-state, .elveos-progress-bar-label {\
    display: block;\
    height: 16px;\
    margin: 0;\
    padding: 0;\
}\
\
.elveos-progress-bar-background {\
    background: -moz-linear-gradient(90deg, rgb(196,196,196), rgb(230,230,230)) repeat scroll 0 0 transparent;\
    margin-bottom: 0;\
    width: 100%;\
}\
\
.elveos-progress-bar-state {\
    background: -moz-linear-gradient(90deg, rgb(0,0,0), rgb(73,73,73)) repeat scroll 0 0 transparent;\
    margin-top: -16px;\
}\
\
.elveos-progress-bar-label {\
    color: white;\
    font-size: 11px;\
    margin-top: -15px;\
    text-align: center;\
    width: 100%;\
}\
\
.elveos-feature-title {\
    font-size: 20px;\
    color: rgb(51,51,51)\
}\
\
.elveos-progress-text {\
    color: black;\
    display: inline-block;\
    font-size: 13px;\
    text-align: center;\
    width: 100%;\
}\
\
.elveos-important {\
    font-size: 14px;\
    font-weight: bold;\
    margin-left: 1em;\
    margin-right: 1em;\
}\
\
.elveos-button {\
    display: block;\
    background: -moz-linear-gradient(90deg, rgb(0,0,0), rgb(73,73,73)) repeat scroll 0 0 transparent;\
    border: 1px solid rgb(78,78,78);\
    border-radius: 10px;\
    color: white;\
    cursor: pointer;\
    font-size: 16px;\
    text-align:center;\
    padding-top: 5px;\
    margin-left: 20px;\
    margin-right: 20px;\
    padding-bottom: 5px;\
    text-decoration: none;\
    width: 150px;\
}\
.elveos-feature-three-column {\
    width: 100%;\
}\
.elveos-feature-leftcolumn, .elveos-feature-middlecolumn, .elveos-feature-rightcolumn {\
    display: table-cell;\
    vertical-align: middle;\
}\
.elveos-info {\
    font-weight: bold;\
    font-size: 13px;\
    width: 200px;\
}\
.elveos-info a{\
    color: rgb(51,51,51);\
    text-decoration: none;\
}\
.elveos-info .elveos-label {\
    font-size: 12px;\
    font-weight: normal;\
}\
.elveos-more-info a {\
    font-size: 13px;\
    color: rgb(0,0,168);\
    text-decoration: none;\
}\
\
.elveos-feature-leftcolumn {\
    width: 100%;\
    padding-left: 25px;\
    padding-right: 35px;\
}\
</style>\
 ";

}

function elveos_generateFeature(feature) {
    var contribution = parseFloat(feature.getElementsByTagName('contribution')[0].firstChild.data);
    var progression = parseFloat(feature.getAttribute("progression"));
    var title = feature.getElementsByTagName('title')[0].firstChild.data;

    var actorName = null;
    var offerAmount = null;
    var actorId = null;
    var isTeam = false;
    if(feature.getElementsByTagName('selectedOffer').length > 0) {
        var selectedOffer =  feature.getElementsByTagName('selectedOffer')[0];
        actorName = selectedOffer.getAttribute("actorName");
        actorId = selectedOffer.getElementsByTagName('author')[0].firstChild.data;
        offerAmount = parseInt(selectedOffer.getAttribute("amount"));
        
        var date = new Date(Date.parse(selectedOffer.getAttribute("expirationDate")));
        
        if(selectedOffer.getElementsByTagName('asteam').length > 0) {
            actorId = selectedOffer.getElementsByTagName('asteam')[0].firstChild.data;
            isTeam = true;
        }
    }    
    
    var id = feature.getAttribute("id");
    var featureState = feature.getAttribute("featureState");

    var state = "funding";
    var progress = parseInt(progression);
    progress = progress - progress%10;
   
    if(contribution == 0 || featureState == "PENDING") {
        progression = 0;
    } else if (featureState == "FINISHED") {
        state = "success";
        progression = 100;
    } else if (featureState == "DEVELOPPING") {
        state = "development";
        progression = 100;
    } else if (progress > 100) {
        progression = 100;
    } 

    var out ="";
    out+="\
<div class=elveos-feature>\
   <div class=elveos-feature-three-column>\
        <div class=elveos-feature-leftcolumn>\
            <p class=elveos-feature-title>"+title+"</p>\
            <div class=elveos-progress-bar-block>\
                <div class=elveos-progress-bar-background></div>\
                <div class=elveos-progress-bar-state style=\"width: "+progression+"%;\"></div>";
    
     if (state == "success") {
        out+="\
                <div class=elveos-progress-bar-label >Funding success</div>";
    } else if (state == "development") {
        out+="\
                <div class=elveos-progress-bar-label >Development ongoing</div>";
    }
    
    out+="\
            </div>\
            <p class=elveos-progress-text>";
    if(offerAmount == null) {
        out+="\
                <span class=elveos-important>"+contribution+"&nbsp;€</span>funded</p>";
    } else {
        out+="\
                <span class=elveos-important>"+contribution+"&nbsp;€</span> i.e <span class=elveos-important>"+progress+"&nbsp;%</span> of <span class=elveos-important>"+offerAmount+"&nbsp;€</span> requested</p>";
    }
    out+="\
        </div>\
        <div class=elveos-feature-middlecolumn>";
    if(actorName != null) {
        //There is an offer
        out+="\
            <p class=elveos-info><span class=elveos-label>Developped by:&nbsp;</span><a href=\""+elveos_hostname+"/"+(isTeam?"teams":"members")+"/"+actorId+"\">"+actorName+"</a></p>\
            <p class=elveos-info><span class=elveos-label>Delivery date:&nbsp;</span>"+date.toLocaleDateString()+"</p>\
            <p class=elveos-more-info><a href=\""+elveos_hostname+"/features/"+id+"/description\">More info ...</a></p>";
    } else {
        out+="\
            <a class=elveos-button href=\""+elveos_hostname+"/features/"+id+"/offers\">Make an offer</a>";
    }
    out+="\
        </div>\
        <div class=elveos-feature-rightcolumn>";
    if(state == "funding") {
        out+="\
        <a class=elveos-button href=\""+elveos_hostname+"/contribution/process?feature="+id+"\">Finance It</a>";
    } else if (state == "success") {
        out+="\
        <a class=elveos-button href=\""+elveos_hostname+"/features/"+id+"/description\"\">More info</a>";
    } else if (state == "development") {
        out+="\
        <a class=elveos-button href=\""+elveos_hostname+"/features/"+id+"/description\"\">More info</a>";
    }
    out+="\
        </div>\
   </div>\
</div>\
";
    return out;
}

function elveos_startGenerateFeatureList(featureListElement) {
    var softwareId = featureListElement.getAttribute('data-software-id');
    newFeatureListElement = document.createElement("div");
    featureListElement.parentNode.replaceChild(newFeatureListElement, featureListElement);

    elveos_ajax(elveos_hostname + '/rest/features?software='+softwareId, function(xml) {

        //Add css
        var html = elveos_getCss();

        var features = xml.getElementsByTagName('features')[0].childNodes;
        for(var i = 0; i < features.length; i++) {
            if(features[i].nodeType ==1) {
                html += elveos_generateFeature(features[i]);
            }
        }

        newFeatureListElement.innerHTML = html;
    }
    );
}

(function () {
    var featureLists = document.getElementsByClassName('elveos-feature-list');
    var featureList;
    for(var i = 0; ((featureList = featureLists[i]) != null); i++) {
        
        if(featureList != undefined  && featureList.getAttribute('data-generated') != 'true') {   
            featureList.setAttribute('data-generated', 'true');
            elveos_startGenerateFeatureList(featureList);
        }
    }  
})();
