//Settings
var protocol = "${protocol}";
var hostname = "${hostname}";
var followText = "${follow_text}";
var followingText = "${following_text}";
var unfollowText = "${unfollow_text}";

function elveos_FollowAllButton(elementId, memberId) {
    
    this.memberId = memberId;
    
    this.init = function(elementId) {
        this.button = document.getElementById(elementId);
        
        this.isSelected = false;
        this.isMailSelected = false;
        
        var links = this.button.getElementsByTagName("a");
        this.followButton = links[0];
        this.followMailButton = links[1];
        
        this.updateFromCss();
        this.instrument();
        
    }
    
    this.updateFromCss = function() {
        if(this.followButton.className == "follow-unselected-without-mail") {
            this.isSelected = false;
            this.isMailSelected = false;
        } else {
            this.isSelected = true;
            if(this.followMailButton.className == "follow-unselected-with-mail") {
                this.isMailSelected = false;
            } else {
                this.isMailSelected = true;
            }
        }
    }
    
    this.updateCss = function() {
        if(this.isSelected) {
        
            this.followButton.className = "follow-selected-without-mail";
            this.followButton.innerHTML = followingText;
            
            if(this.isMailSelected) {
                this.followMailButton.className = "follow-selected-with-mail";
            } else {
                this.followMailButton.className = "follow-unselected-with-mail";
            }
        } else {
            this.followButton.innerHTML = followText;
        
            this.followButton.className = "follow-unselected-without-mail";
            this.followMailButton.className = "follow-unselected-with-mail";
        }
    }
    
    this.instrument = function() {
        var that = this;
        this.followButton.onclick = function() {that.onSelectButtonClick(); return false;}
        this.followMailButton.onclick = function() {that.onSelectMailButtonClick(); return false;}
        this.followButton.onmouseover = function() {if(that.isSelected) {that.followButton.innerHTML = unfollowText; that.followMailButton.className = "follow-unselect-with-mail"; } return true;}
        this.followButton.onmouseout = function() {
            if(that.isSelected) {
                that.followButton.innerHTML = followText;
            } else {
                that.followButton.innerHTML = followingText;
            }
            that.updateCss();
             return true;
         }
        this.followMailButton.onmouseover = function() {
                if(!that.isMailSelected) {that.followButton.className = "follow-selected-without-mail"; } return true;
            }
        this.followMailButton.onmouseout = function() {that.updateCss(); return true;}
    }
    
    this.onSelectButtonClick = function() {
        var that = this;
        if(this.isSelected) {
            this.ajax('PUT', "/rest/members/"+this.memberId+"/setfollow?followall=false&followallwithmail=false", function(response) {that.onUnfollowResponse(response);});
        } else {
            this.ajax('PUT', "/rest/members/"+this.memberId+"/setfollow?followall=true&followallwithmail=false", function(response) {that.onFollowResponse(response);});
        }
    }
    
    this.onSelectMailButtonClick = function() {
        var that = this;
        if(this.isMailSelected) {
            this.ajax('PUT', "/rest/members/"+this.memberId+"/setfollow?followall=true&followallwithmail=false", function(response) {that.onFollowResponse(response);});
        } else {
            this.ajax('PUT', "/rest/members/"+this.memberId+"/setfollow?followall=true&followallwithmail=true", function(response) {that.onFollowResponse(response);});
        }
    }
    
    this.onFollowResponse = function(response) {
        if(response.getElementsByTagName("rest")[0].getAttribute("result")=="ok") {
            var rest = response.getElementsByTagName("rest")[0];
            var member = rest.getElementsByTagName("member")[0];
            
            this.isSelected = (member.getAttribute("followall") == 'true');
            this.isMailSelected = (member.getAttribute("followallwithmail") == 'true');
            this.updateCss();
            this.instrument();
        }
    }
    
    this.onUnfollowResponse = function(response) {
        if(response.getElementsByTagName("rest")[0].getAttribute("result")=="ok") {
            this.isSelected = false;
            this.isMailSelected = false;
            this.updateCss();
            this.instrument();
        }
    }
    
    this.ajax = function (method, url, callback) {
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
        xhr.open(method, protocol+'://'+hostname+ url, true);                  
        xhr.send(null);
    }
    
       
    this.init(elementId)
}

function elveos_bindFollowAllButton(elementId, memberId) {
    new elveos_FollowAllButton(elementId, memberId);
}




