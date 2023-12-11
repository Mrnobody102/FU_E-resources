$(document).ready(function() {
    // When the export button is clicked
    $("#exportButton").click(function() {
        // Toggle the visibility of the export form container
        $("#exportFormContainer").toggle();
    });

    // When the close button is clicked
    $("#closeButton").click(function() {
        // Hide the export form container
        $("#exportFormContainer").hide();
    });

    // Handle form submission
    $("#exportForm").submit(function(event) {
        // Perform export logic here (you can use AJAX to send form data to the server for processing)
        console.log("Exporting to Excel...");

        // After exporting, hide the form
        $("#exportFormContainer").hide();
    });
});