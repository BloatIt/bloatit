function round2(value) {
  return parseFloat((value).toFixed(2));
}


//Settings
var chargeField = "${charge_field_id}";
var targetField = "${target_field}";
var commissionVariableRate = ${commission_variable_rate};
var commissionFixRate = ${commission_fix_rate};

quotationEntries = new Array();

function update() {
    var amount = parseFloat($( "#"+chargeField ).val());

    if(isNaN(amount)) {
        return
    }

    var total =  amount + amount * commissionVariableRate + commissionFixRate;
    $("#"+targetField).html(round2(total) + "&nbsp;€")
};

$( "#"+chargeField).bind('input', update);
