//Settings
var protocol = "${protocol}";
var hostname = "${hostname}";
var followText = "${follow_text}";
var followingText = "${following_text}";
var unfollowText = "${unfollow_text}";

function elveos_FollowButton(elementId, memberId, featureId, isFeatureComments, isBugComments) {
    

    this.memberId = memberId;
    this.featureId = featureId;
    this.isFeatureComments = isFeatureComments;
    this.isBugComments = isBugComments;
    
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
    
    this.instrument = function() {
        var that = this;
        this.followButton.onclick = function() {that.onSelectButtonClick(); return false;}
        this.followMailButton.onclick = function() {that.onSelectMailButtonClick(); return false;}
        this.followButton.onmouseover = function() {if(that.isSelected) {that.followButton.innerHTML = unfollowText; } return true;}
        this.followButton.onmouseout = function() {
            if(that.isSelected) {
                that.followButton.innerHTML = followText;
            } else {
                that.followButton.innerHTML = followingText;
            }
             return true;
         }
    }
    
    this.onSelectButtonClick = function() {
        var that = this;
        if(this.isSelected) {
            this.ajax('DELETE', "/rest/followfeatures?follower="+this.memberId+"&followed="+this.featureId, function(response) {that.onUnfollowResponse(response);});
        } else {
            this.ajax('PUT', "/rest/followfeatures?follower="+this.memberId+"&followed="+this.featureId+"&mail=false&featureComment=true&bugComment=true", function(response) {that.onFollowResponse(response);});
        }
    }
    
    this.onSelectMailButtonClick = function() {
        var that = this;
        if(this.isMailSelected) {
            this.ajax('PUT', "/rest/followfeatures?follower="+this.memberId+"&followed="+this.featureId+"&mail=false&featureComment="+this.isFeatureComments+"&bugComment="+this.isBugComments, function(response) {that.onFollowResponse(response);});
        } else {
            this.ajax('PUT', "/rest/followfeatures?follower="+this.memberId+"&followed="+this.featureId+"&mail=true&featureComment="+this.isFeatureComments+"&bugComment="+this.isBugComments, function(response) {that.onFollowResponse(response);});
        }
    }
    
    this.onFollowResponse = function(response) {
        if(response.getElementsByTagName("rest")[0].getAttribute("result")=="ok") {
            var rest = response.getElementsByTagName("rest")[0];
            var follow = rest.getElementsByTagName("followfeature")[0];
            
            this.isSelected = true;
            this.isMailSelected = (follow.getAttribute("mail") == 'true');
            this.isFeatureComments = (follow.getAttribute("featureComment") == 'true');
            this.isBugComments = (follow.getAttribute("bugComment") == 'true');
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

function elveos_bindFollowButton(elementId, memberId, featureId, isFeatureComments, isBugComments) {
    new elveos_FollowButton(elementId, memberId, featureId, isFeatureComments, isBugComments);
}




