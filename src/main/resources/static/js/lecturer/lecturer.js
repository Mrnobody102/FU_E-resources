function getAllCourse() {
    $("#setStatus").val("all");
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}

function getNewCourse() {
    $("#setStatus").val("new");
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}

function getPublishCourse() {
    $("#setStatus").val("publish");
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}

function getDraftCourse() {
    $("#setStatus").val("draft");
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}

function getHideCourse() {
    $("#setStatus").val("hide");
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}

function pagingInMyManageCourse(clickedElement) {
    var page = $(clickedElement).attr('page');
    $("#pageIndex").val(page);
    $("#form-get-manage-course").submit();
}

// function viewQuestionWaiting() {
//     $(".lec__questions-list-view").css("display", "none");
//     var divContent = $("#lec__view-waiting-for-reply-question").text().trim();
//     if (divContent.length == 0) {
//         $("#loading").css("display", "block");
//         $.ajax({
//             type: 'GET',
//             url: '/api/lecturer/my_question/new_question',
//             dataType: 'json',
//             success: function (data) {
//                 console.log(data.length)
//                 var html = "";
//                 for (let i = 0; i < data.length; i++) {
//                     html +="<div class=lec__question-content-wrapper>\n" +
//                         "                        <span class=\"lec__question-date\">"+data[i].lastModifiedDate+"</span><br>\n" +
//                         "                    <a class=\"lec__question-title\">"+ data[i].studentName +" asked on " + data[i].documentTitle+"</a>\n" +
//                         "                    <p class=\"lecturer-content-view-brief\"><span>"+data[i].questionContent+"</span>\n" +
//                         "                        <a class=\"link-view-detailed\"\n" +
//                         "                              href=\"/lecturer/documents/" + data[i].documentId + "#"+ data[i].questionId+ "\">view <i\n" +
//                         "                            class=\"fa-solid fa-arrow-right\"></i></a></p>\n" +
//                         "                    </div>";
//
//                 }
//                 $("#lec__view-waiting-for-reply-question").html(html);
//                 $("#loading").css("display", "none");
//                 $("#lec__view-waiting-for-reply-question").css("display", "block");
//             },
//             error: function (xhr) {
//                 // Handle errors
//             }
//         });
//     } else $("#lec__view-waiting-for-reply-question").css("display", "block");
//
// }
// function viewQuestionReplied(){
//     $(".lec__questions-list-view").css("display", "none");
//     var divContent = $("#lec__view-replied-question").text().trim();
//     if (divContent.length == 0) {
//         $("#loading").css("display", "block");
//         $.ajax({
//             type: 'GET',
//             url: '/api/lecturer/my_question/replied_question',
//             dataType: 'json',
//             success: function (data) {
//                 console.log(data.length)
//                 var html = "";
//                 for (let i = 0; i < data.length; i++) {
//                     html +="<div class=lec__question-content-wrapper>\n" +
//                         "                        <span class=\"lec__question-date\">"+data[i].lastModifiedDate+"</span><br>\n" +
//                         "                    <a class=\"lec__question-title\">"+ data[i].studentName +" asked on " + data[i].documentTitle+"</a>\n" +
//                         "                    <p class=\"lecturer-content-view-brief\"><span>"+data[i].questionContent+"</span>\n" +
//                         "                        <a class=\"link-view-detailed\"\n" +
//                         "                              href=\"/lecturer/documents/" + data[i].documentId + "#"+ data[i].questionId+ "\">view <i\n" +
//                         "                            class=\"fa-solid fa-arrow-right\"></i></a></p>\n" +
//                         "                    </div>";
//
//                 }
//                 $("#lec__view-replied-question").html(html);
//                 $("#loading").css("display", "none");
//                 $("#lec__view-replied-question").css("display", "block");
//             },
//             error: function (xhr) {
//                 // Handle errors
//             }
//         });
//     } else $("#lec__view-replied-question").css("display", "block");
// }
function submitFormCourseManage(e) {
    $("#pageIndex").val("1");
    $("#form-get-manage-course").submit();
}
function viewAllQuestion() {
    $("#setStatus").val("all");
    $("#pageIndex").val("1");
    $("#getMyQuestion").submit();
}
function viewQuestionWaiting() {
    $("#setStatus").val("wait-reply");
    $("#pageIndex").val("1");
    $("#getMyQuestion").submit();
}
function viewQuestionReplied() {
    $("#setStatus").val("replied");
    $("#pageIndex").val("1");
    $("#getMyQuestion").submit();
}
function pagingInMyQuestion(clickedElement) {
    var page = $(clickedElement).attr('page');
    $("#pageIndex").val(page);
    $("#getMyQuestion").submit();
}
function submitFormGetQuestion() {
    $("#pageIndex").val("1");
    $("#getMyQuestion").submit();
}
// Document scope
$(document).ready(function () {
    // $("#button-form-get-manage-course").on('click', function (e){
    //     $("#pageIndex").val("1");
    //     $("#form-get-manage-course").submit();
    // })

    $("#form-get-manage-course :input").keypress(function (event) {
        // Kiểm tra xem phím được ấn có phải là Enter không (mã ASCII 13)
        if (event.which === 13) {
            $("#pageIndex").val("1");
            $("#form-get-manage-course").submit();
        }
    });
    $("#getMyQuestion :input").keypress(function (event) {
        // Kiểm tra xem phím được ấn có phải là Enter không (mã ASCII 13)
        if (event.which === 13) {
            $("#pageIndex").val("1");
            $("#getMyQuestion").submit();
        }
    });

    // click div course auto click to course name
    $('.home__course-wrapper').on('click', function () {
        $(this).find('.home__course-name').click();
    });
    // add click on <li> also -> click on <a>
    $('.lec__navbar-item').on('click', function () {
        var link = $(this).find('a');
        if (link.length > 0) {
            link[0].click();
        }
    });

    $(".page-course-number").click(function () {

        var search = $("#search-text").val();
        var pageIndex = $(this).html();

        window.location = "/lecturer/courses/list/" + pageIndex + "?search=" + search;

    });

    // Delete course
    $("body").on("click", ".delete-course", function () {
        var courseId = $(this).attr("id");
        var result = confirm("Do you want delete this courses?" + courseId);

        if (result) {
            window.location = "/lecturer/courses/delete/" + courseId;
        }
    });

    // Delete topic
    $("body").on("click", ".delete-topic", function () {
        var courseTopic = $(this).attr("id");
        var result = confirm("Do you want delete this topic?" + courseTopic);
        if (result) {
            window.location = "/lecturer/courses/deleteTopic" + courseTopic;
        }
    });
    /*
        NAVBAR
     */

    // Navbar when choose
    let path = window.location.pathname;
    let $targetElements = $('.header__navbar-list > li > a[href="' + path + '"]');

    $targetElements.removeClass('header__navbar-item-main-link');
    $targetElements.addClass('header__navbar-item-main-link_active');
    $targetElements.parent().addClass('header__navbar-main-item_active');

    /*
        SIDEBAR
     */

    // Show sidebar
    const showSidebar = (toggleId, sidebarId, mainId) => {
        const toggle = document.getElementById(toggleId),
            sidebar = document.getElementById(sidebarId),
            main = document.getElementById(mainId)

        if (toggle && sidebar && main) {
            toggle.addEventListener('click', () => {
                /* Show sidebar */
                sidebar.classList.toggle('show-sidebar')
                /* Add padding main */
                main.classList.toggle('main-pd')

            })
        }
    }
    showSidebar('toggleIdLecturer', 'sidebar-lecturer', 'main-lecturer')

    // Link active
    const sidebarLink = document.querySelectorAll('.sidebar__link')

    function linkColor() {
        sidebarLink.forEach(l => l.classList.remove('active-link'))
        this.classList.add('active-link')
    }

    sidebarLink.forEach(l => l.addEventListener('click', linkColor))


    /*
        PAGINATION
     */

    $("body").on("click", ".lec__navbar-item", function () {
        $(".lec__navbar-item").removeClass("lec__navbar-active");
        $(this).addClass("lec__navbar-active");
    })
});