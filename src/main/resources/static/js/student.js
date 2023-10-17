function toggleDropdown() {
    const dropdownContent = document.querySelector('.dropdown-content');
    if (dropdownContent.style.display === "none" || dropdownContent.style.display === "") {
        dropdownContent.style.display = "block";
        setTimeout(function() {
            dropdownContent.style.opacity = "1";
        }, 10); // delay a bit to ensure the transition takes effect
    } else {
        dropdownContent.style.opacity = "0";
        setTimeout(function() {
            dropdownContent.style.display = "none";
        }, 300); // delay to wait for the transition to finish
    }
}