// script.js

// Xử lý sự kiện khi nút thêm tài liệu được nhấn
// document.querySelector('form').addEventListener('submit', function (event) {
//     event.preventDefault();
//     // Thêm mã JavaScript xử lý khi nút được nhấn
//     alert('Tài liệu đã được thêm thành công!');
// });

$(document).ready(function() {

    $(".page-number").click(function() {

        var search = $("#search-text").val();
        var pageIndex = $(this).html();

        // alert("/librarian/accounts/" + pageIndex + "?search=" + search);

        window.location = "/librarian/accounts/list/" + pageIndex + "?search=" + search;

    });

    $(".delete-account").click(function(){

        var accountId = $(this).attr("id");

        var result = confirm("Do you want delete this account?" + accountId);

        if(result){
            window.location = "/librarian/accounts/delete/"+accountId;
        }
    });

    $("body").on("click", ".delete-course", function() {
        var courseId = $(this).attr("id");
        var result = confirm("Do you want delete this courses?" + courseId);

        if(result){
            window.location = "/librarian/courses/delete/"+courseId;
        }
    });

    $("body").on("click", ".delete-topic", function() {
        var courseTopic = $(this).attr("id");
        var result = confirm("Do you want delete this topic?"+ courseTopic);
        if(result){
            window.location = "/librarian/courses/deleteTopic"+courseTopic;
        }
    });
});