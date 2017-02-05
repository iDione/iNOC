$(document).ready(function () {
    function moveUsers(origin, dest) {
        $(origin).find(':selected').appendTo(dest);
    };

    $('#addUser').click(function(e) {
    	e.preventDefault();
    	moveUsers('#allPocUserIds', '#pocUserIds');
    });

    $('#removeUser').click(function(e) {
    	e.preventDefault();
    	moveUsers('#pocUserIds', '#allPocUserIds');
    });

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
    	$('#pocUserIds option').prop('selected', true);
    });

    $("#createFilter").click(function (e) {
    	$('#keywords option').prop('selected', true);
    	$('#pocUserIds option').prop('selected', true);
    });

    $("#addUser").click();
});