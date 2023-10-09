// script.js

// Xử lý sự kiện khi nút thêm tài liệu được nhấn
document.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault();
    // Thêm mã JavaScript xử lý khi nút được nhấn
    alert('Tài liệu đã được thêm thành công!');
});


$(document).ready(function() {

    // $(".page-number").click(function() {
    //
    //     var firstName = $("#firstName").val();
    //     var lastName = $("#lastName").val();
    //
    //     var pageIndex = $(this).html();
    //
    //     alert("/admin/contacts/" + pageIndex + "?firstName=" + firstName + "&lastName=" + lastName);
    //
    //     window.location = "/admin/contacts/" + pageIndex + "?firstName=" + firstName + "&lastName=" + lastName;
    //
    // });


    $(".delete-account").click(function(){

        var accountId = $(this).attr("id");

        var result = confirm("Do you want delete this account?" + accountId);

        if(result){
            window.location = "/librarian/accounts/delete/"+accountId;
        }
    });

});