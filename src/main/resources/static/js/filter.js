$(document).ready(function () {
    $("#addKeyword").click(function (e) {
    	e.preventDefault();
        if ($('#keyword').val() != "") {
            $('#keywords').append('<option value="' + $('#keyword').val() + '">' + $('#keyword').val() + '</option>');
            $('#keyword').val("");
         }else {
            alert('Please enter a value and then proceed.');
            $('#keyword').focus();
         }
    });
    
    $("#removeKeyword").click(function (e) {
    	e.preventDefault();
    	$("#keywords option:selected").remove();
    });
    
    $("#updateFilter").click(function (e) {
    	$('#keywords option').prop('selected', true);
    });
    
    $("#createFilter").click(function (e) {
    	$('#keywords option').prop('selected', true);
    });
});