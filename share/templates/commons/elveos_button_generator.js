//Settings
var featureId = "${feature_id}";
var protocol = "${protocol}";

var host = protocol+'://'+ window.location.hostname;

function update_output() {
    var length = $("input[name=elveos_button_length]:checked").val();
    var height= $("input[name=elveos_button_height]:checked").val();
    
    var output = "<a title=\"Pay with Elveos\" class=\"elveos-button\" href=\""+host+"/f/"+featureId+"\" data-button-style=\""+length+"_"+height+"\" data-feature-id=\""+featureId+"\"></a>";
    
    output += "<script type=\"text/javascript\" src=\""+host+"/resources/commons/api/button.js\"></scr"+"ipt>";
    $("textarea#elveos_button_generator_output").val(output);
    $("div#elveos_button_generator_output_example").html(output);
};

(function () {
    $("input[name=elveos_button_length]").change(update_output);
    $("input[name=elveos_button_height]").change(update_output);
    update_output();
})();

