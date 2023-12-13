function viewOtherDocument(param) {
    window.location = "/student/documents/" + param;
}

function viewAllQuestion() {
    $(".stu__questions-list-view").css("display", "none");
    $("#stu__view-question-history").css("display", "block");
}

function viewQuestionWaiting() {
    $(".stu__questions-list-view").css("display", "none");
    var divContent = $("#stu__view-waiting-for-reply-question").text().trim();
    if (divContent.length == 0) {
        $("#loading").css("display", "block");
        $.ajax({
            type: 'GET',
            url: '/api/student/my_question/new_question',
            dataType: 'json',
            success: function (data) {
                console.log(data.length)
                var html = "";
                for (let i = 0; i < data.length; i++) {
                    html += "<div class=stu__question-content-wrapper>\n" +
                        "                        <span class=\"stu__question-content stu__question-date\">" + data[i].lastModifiedDate + "</span><br>\n" +
                        "                    <a class=\"stu__question-title\">You asked on" + data[i].documentTitle + "</a>\n" +
                        "                    <p class=\"student-content-view-brief\"><span>" + data[i].questionContent + "</span>\n" +
                        "                        <a class=\"link-view-detailed\"\n" +
                        "                              href=\"/student/documents/" + data[i].documentId + "#" + data[i].questionId + "\">view <i\n" +
                        "                            class=\"fa-solid fa-arrow-right\"></i></a></p>\n" +
                        "                    </div>";

                }
                $("#stu__view-waiting-for-reply-question").html(html);
                $("#loading").css("display", "none");
                $("#stu__view-waiting-for-reply-question").css("display", "block");
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    } else $("#stu__view-waiting-for-reply-question").css("display", "block");

}

function viewNewReplyQuestion() {

    $(".stu__questions-list-view").css("display", "none");
    var divContent = $("#stu__view-new-reply-question").text().trim();
    if (divContent.length == 0) {
        $("#loading").css("display", "block");
        $.ajax({
            type: 'GET',
            url: '/api/student/my_question/new_replies',
            dataType: 'json',
            success: function (data) {
                console.log(data.length)
                var html = "";
                for (let i = 0; i < data.length; i++) {
                    html += "<div class=stu__question-content-wrapper>\n" +
                        "                        <span class=\"stu__question-content stu__question-date\">" + data[i].lastModifiedDate + "</span><br>\n" +
                        "                    <a class=\"stu__question-title\">New reply for your question at " + data[i].documentTitle + "</a>\n" +
                        "                    <p class=\"student-content-view-brief\"><span>" + data[i].questionContent + "</span>\n" +
                        "                        <a class=\"link-view-detailed\"\n" +
                        "                              href=\"/student/documents/" + data[i].documentId + "#" + data[i].questionId + "\">view <i\n" +
                        "                            class=\"fa-solid fa-arrow-right\"></i></a></p>\n" +
                        "                    </div>";
                }
                $("#stu__view-new-reply-question").html(html);
                $("#loading").css("display", "none");
                $("#stu__view-new-reply-question").css("display", "block");
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    } else $("#stu__view-new-reply-question").css("display", "block");

}

function viewTopicDocument(param) {
    if ($("#list-doc-of-topic-" + param).css('display') !== 'none') {
        $("#view-less-" + param).css("display", "none");
        $("#view-more-" + param).css("display", "inline");
        $("#list-doc-of-topic-" + param).css("display", "none");
    } else {
        var divContent = $("#list-doc-of-topic-" + param).text().trim();
        console.log(divContent)
        if (divContent.length == 0) {
            $("#view-less-" + param).css("display", "none");
            $("#view-more-" + param).css("display", "none");
            $("#loading-more-document-" + param).css("display", "inline");
            $.ajax({
                type: 'GET',
                url: '/api/student/documents/get_by_topic/' + param,
                dataType: 'json',
                success: function (data) {
                    console.log(data.length)
                    var html = "";
                    for (let i = 0; i < data.length; i++) {
                        html += "<div class=\"d-flex document-view-info border-bottom\">\n" +
                            "                                                    <div class=\"doc-info-head grid__column-8\">\n" +
                            "                                                        <p class=\"doc-info-title\"><a\n" +
                            "                                                                href=\"/student/documents/" + data[i].id + "\">\n" +
                            "                                                            <span>" + data[i].title + "</span></a>\n" +
                            "                                                        </p>\n" +
                            "                                                        <p class=\"doc-info-description\">\n" +
                            "                                                            <span>" + data[i].description + "</span>\n" +
                            "                                                        </p>\n" +
                            "                                                    </div>\n" +
                            "                                                    <div class=\"doc-info grid__column-2\">\n" +
                            "                                                        <p><span class=\"doc-info-date\">" + data[i].lastModifiedDate + "</span></p>\n" +
                            "                                                    </div>\n" +
                            "                                                </div>"

                    }
                    $("#list-doc-of-topic-" + param).html(html);
                    $("#view-less-" + param).css("display", "inline");
                    $("#loading-more-document-" + param).css("display", "none");
                    $("#list-doc-of-topic-" + param).css("display", "block");
                },
                error: function (xhr) {
                    // Handle errors
                }
            });
        } else {
            $("#view-less-" + param).css("display", "inline");
            $("#view-more-" + param).css("display", "none");
            $("#loading-more-document-" + param).css("display", "none");
            $("#list-doc-of-topic-" + param).css("display", "block");
        }
    }
}

$(document).ready(function () {

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
    showSidebar('toggle', 'sidebar', 'main')

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

    // Account pagination
    $(".page-stu-course-number").click(function () {

        const search = $("#search-text").val();
        const filter = $("#filter").val();
        let pageIndex = $(this).html();

        window.location = "/student/search_course/" + pageIndex + "?search=" + search;
    });

    $(".previous-page-stu-course-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-course-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/student/search_course/" + (currentPage - 1) + "?search=" + search;
        }
    });

    $(".next-page-stu-course-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-course-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/student/search_course/" + (currentPage + 1) + "?search=" + search;
    });


    // Account pagination
    $(".page-stu-note-number").click(function () {

        const search = $("#search-text").val();
        const filter = $("#filter").val();
        let pageIndex = $(this).html();

        window.location = "/student/my_library/my_notes/" + pageIndex + "?search=" + search + "&filter=" + filter;
    });

    $(".previous-page-stu-note-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-note-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/student/my_library/my_notes/" + (currentPage - 1) + "?search=" + search + "&filter=" + filter;
        }
    });

    $(".next-page-stu-note-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-note-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/student/my_library/my_notes/" + (currentPage + 1) + "?search=" + search + "&filter=" + filter;
    });


    // add click on <li> also -> click on <a>
    $('.stu__navbar-favourite-item').on('click', function() {
        var link = $(this).find('a');
        if (link.length > 0) {
            link[0].click();
        }
    });


    $("body").on("click", ".save-doc", function () {
        var docId = $(this).attr("docId");
        var loading = "<p><a docId=" + docId + ">" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        Bookmarking</a></p>"
        $(".stu_save-doc-link").html(loading);
        $.get({
            type: 'POST',
            url: '/api/student/documents/' + docId + '/save_document',
            success: function (responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";

                $(".stu_save-doc-link").html(saved);
            },
            error: function (errorData) {
                var unsaved = "<p><a class=\"save-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";
                $(".stu_save-doc-link").html(unsaved);
            }
        })
    });
    $("body").on("click", ".unsaved-doc", function () {
        var docId = $(this).attr("docId");
        var loading = "<p><a th:attr=\"docId=${docId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        UnBookmarking</a></p>"
        $(".stu_save-doc-link").html(loading);
        $.get({
            type: 'POST',
            url: '/api/student/documents/' + docId + '/unsaved_document',
            success: function (responseData) {
                console.log(responseData);

                var unsaved = "<p><a class=\"save-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";


                $(".stu_save-doc-link").html(unsaved);
            },
            error: function (errorData) {
                var saved = "<p><a class=\"unsaved-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                $(".stu_save-doc-link").html(saved);
            }
        })
    });
    $("body").on("click", ".save-course", function () {
        var courseId = $(this).attr("courseId");
        console.log(courseId)
        var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        Bookmarking</a></p>"
        $(".stu_save-course-link").html(loading);
        $.get({
            type: 'POST',
            url: '/api/student/courses/' + courseId + '/save_course',
            success: function (responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                $(".stu_save-course-link").html(saved);
            },
            error: function (errorData) {
                console.log(errorData)
                var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";
                $(".stu_save-course-link").html(unsaved);
            }
        })
    })

    $("body").on("click", ".unsaved-course", function () {
        var courseId = $(this).attr("courseId");
        var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        UnBookmarking</a></p>"
        $(".stu_save-course-link").html(loading);
        $.get({
            type: 'POST',
            url: '/api/student/courses/' + courseId + '/unsaved_course',
            success: function (responseData) {
                var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";
                $(".stu_save-course-link").html(unsaved);

            },
            error: function (errorData) {
                var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                $(".stu_save-course-link").html(saved);

            }
        })
    })
    $("body").on("click", ".stu__navbar-favourite-item", function () {
        $(".stu__navbar-favourite-item").removeClass("stu__navbar-active");
        $(this).addClass("stu__navbar-active");
    })
    // Add event listeners to input and textarea
    $('#noteTitle').on('input', function () {
        $('#send-edit-note-button').removeClass('disabled');
    });
    $('#description').on('input', function () {
        $('#send-edit-note-button').removeClass('disabled');
    });
});

function viewDocumentNote() {
    console.log("view my note")
    $("#documentNote").removeClass('display-none');
    $("#myNote").addClass('display-none');
}

function viewMyNote() {
    console.log("view document note")
    $("#documentNote").addClass('display-none');
    $("#myNote").removeClass('display-none');
}

function submitDeleteMyNote() {
    var result = window.confirm("Do you want to delete your note?");
    if (result) {
        $("#send-edit-note-button").addClass("disabled");
        $("#send-delete-note-button").addClass("display-none");
        $("#sending-delete").css("display", "inline");
        $("#deleteStudentNote").submit();
        console.log("submited.")
    }
}

function choseResourceType(label) {
    $(".link-view-detail-topic").removeClass("link-active");
    $("#list-by-topic").addClass("display-none");
    var resourceId = $(label).attr('for').slice(0, -12);
    var courseId = $(label).attr('course-id');
    $("#loading-by-resource").css("display", "block");
    $("#list-by-resource").html("");
    $.ajax({
        type: 'GET',
        url: '/api/student/documents/get_by_resource/' + resourceId + "/" + courseId,
        dataType: 'json',
        success: function (data) {
            displayDocumentsByResource(data);
        },
        error: function (xhr) {
            console.log("Fail")
        }
    });
}

function displayDocumentsByResource(dataMap) {
    var html = "";
    $.each(dataMap, function (topic, documents) {
        html += "<h2 class='topic-info-title topic-title-in-view-course' onclick= getByTopic(\"" + documents[0].topicId + "\")>" + documents[0].topicTitle + "</h2>";
        html += "<ul>";
        $.each(documents, function (index, document) {
            html += "<div class=\"d-flex document-view-info border-bottom\">\n" +
                "                                                    <div class=\"doc-info-head grid__column-8\">\n" +
                "                                                        <p class=\"doc-info-title\"><a\n" +
                "                                                                href=\"/student/documents/" + document.id + "\">\n" +
                "                                                            <span>" + document.title + "</span></a>\n" +
                "                                                        </p>\n" +
                "                                                        <p class=\"doc-info-description\">\n" +
                "                                                            <span>" + document.description + "</span>\n" +
                "                                                        </p>\n" +
                "                                                    </div>\n" +
                "                                                    <div class=\"doc-info grid__column-2\">\n" +
                "                                                        <p><span class=\"doc-info-date\">" + document.lastModifiedDate + "</span></p>\n" +
                "                                                    </div>\n" +
                "                                                </div>"


        });
        html += "</ul>";
    });
    $("#list-by-resource").html(html);
    $("#loading-by-resource").css("display", "none");
    $("#list-by-resource").removeClass("display-none");
}

function getByTopic(param) {
    $(".link-view-detail-topic").removeClass("link-active");
    $("#link-view-detail-" + param).addClass("link-active");
    $("#formChoseResourceTypeBot input[type='radio']").prop("checked", false);
    $("#formChoseResourceTypeTop input[type='radio']").prop("checked", false);
    console.log(param)
    $("#list-by-resource").html("");
    $("#list-by-topic").addClass("display-none");
    $("#loading-by-resource").css("display", "block");
    $.ajax({
        type: 'GET',
        url: '/api/student/documents/get_by_topic/' + param,
        dataType: 'json',
        success: function (data) {
            console.log(data)
            displayDocumentsByTopic(data);
        },
        error: function (xhr) {
            // Handle errors
            console.log("Fail")
        }
    });
}

function displayDocumentsByTopic(data) {
    var html = ""
    var count = 1;
    $.each(data, function (index, document) {
        if (count == 1) {
            html += "<h2 class='topic-info-title topic-title-in-view-course' onclick= getByTopic(" + document.topicId + ")>" + document.topicTitle + "</h2>";
            html += "<ul>";
        }
        html += "<div class=\"d-flex document-view-info border-bottom\">\n" +
            "                                                    <div class=\"doc-info-head grid__column-8\">\n" +
            "                                                        <p class=\"doc-info-title\"><a\n" +
            "                                                                href=\"/student/documents/" + document.id + "\">\n" +
            "                                                            <span>" + document.title + "</span></a>\n" +
            "                                                        </p>\n" +
            "                                                        <p class=\"doc-info-description\">\n" +
            "                                                            <span>" + document.description + "</span>\n" +
            "                                                        </p>\n" +
            "                                                    </div>\n" +
            "                                                    <div class=\"doc-info grid__column-2\">\n" +
            "                                                        <p><span class=\"doc-info-date\">" + document.lastModifiedDate + "</span></p>\n" +
            "                                                    </div>\n" +
            "                                                </div>"
        if (count == 1) {
            html += "<ul>";
            count = 2;
        }
    });

    $("#list-by-resource").html(html);
    $("#loading-by-resource").css("display", "none");
    $("#list-by-resource").removeClass("display-none");


}

function listByTopic() {
    $(".link-view-detail-topic").removeClass("link-active");
    $("#list-by-topic").removeClass("display-none");
    $("#list-by-resource").addClass("display-none");
}