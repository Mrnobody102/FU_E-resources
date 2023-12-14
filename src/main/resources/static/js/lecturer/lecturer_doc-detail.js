function sendMessage(aId, type, questionContent) {
    stompClient.send('/app/reply', {}, JSON.stringify({answerId: aId, type: type, sendContent: questionContent}));
}

function submitFormReplyQuestion(param) {
    var content = $('#new-reply-content-' + param).val();
    var trimmedString = $.trim(content);
    console.log(content)
    console.log(param)

    if (trimmedString == '') {
        $('#new-reply-content-error-' + param).css("display", "block");
    } else {
        $('#send-reply-button-' + param).css("display", "none");
        $('#exist-reply-form-button-' + param).css("display", "none");
        $('#sending-reply-' + param).css("display", "inline");
        $('#new-reply-content-error-' + param).css("display", "none");
        var replyForm = 'reply-content-form' + param;
        var formData = $("#" + replyForm).serialize();
        console.log(replyForm)
        console.log(formData)
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/answer/add',
            data: formData,
            dataType: 'json',
            success: function (data) {
                $('#reply-content-form' + param).trigger('reset');
                $('#send-reply-button-' + param).css("display", "inline");
                $('#exist-reply-form-button-' + param).css("display", "inline");
                $('#sending-reply-' + param).css("display", "none");
                $('#reply-form' + param).css("display", "none");
                sendMessage(data.answerId, "2", data.answerContent);
                var html = "";
                if (data.studentName == null) {
                    html = "<div class=\"reply-content border-bottom\"  id=\"" + data.answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\" id=\"reply-content-"+ data.answerId +"\">" + data.answerContent + "</p>";
                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data.answerId+ "\" style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data.answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <input class=\"update-reply\" id=\"update-reply-content-"+ data.answerId +"\">\n" +
                        "                                                    <button id=\"close-update-reply-" + data.answerId+ "\" type=\"button\" title=\"exist\"\n" +
                        "                                                            reply-id=\""+ data.answerId+"\" onclick=existFormEditReply(\"" +data.answerId+"\")\n" +
                        "                                                            class=\"exist-form-edit-reply btn-danger\"><i class=\"fa-solid fa-xmark\"></i> Close</button> " +
                        "                                                            <a type=\"button\" class=\"display-none\" title=\"Sending\" id=\"sending-update-reply"+ data.answerId+ "\"><i\n" +
                        "                                                            class=\"fas fa-spinner fa-spin\"></i> Sending...</a>\n" +
                        "                                                    <button type=\"button\" title=\"Edit\" id=\"send-update-reply-"+ data.answerId+"\" reply-id=\"" + data.answerId + "\"\n" +
                        "                                                            onclick=submitFormEditReply(\"" + data.answerId + "\") class=\"btn-save\">" +
                        "                                                    <i class=\"fa-solid fa-paper-plane\"></i> Edit\n" +
                        "                                                    </button>\n" +
                        "                                                    </div>";
                    // date and link
                    html+="<p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.createdDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item edit-reply\" reply-id=\"" + data.answerId + "\">Edit</a> | " +
                        "                     <a class=\"lec__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data.answerId + "\" onclick=deleteReply(\"" + data.answerId + "\",\"" + data.questionId + "\")>Delete</a>\n" +
                        "                     </div>";
                } else {
                    html = "<div class=\"reply-content border-bottom\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data.studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data.answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data.createdDate + "</span> " +
                        "                     </div>";
                }
                var answer = data.answerContent;
                let modified_val = answer.replace(/"/g, '&quot;')
                $("#list-reply-content-" + param).append(html);
                $("#update-reply-content-"+ data.answerId).val(modified_val);
                // change total of answer
                var totalReply = $('#number-reply-' + param).text();
                var intValue = parseInt(totalReply);

                if (!isNaN(intValue)) {
                    // Increment the integer by 1
                    var newValue = intValue + 1;

                    // Set the new value as the text of the span
                    $('#number-reply-' + param).text(newValue);
                } else {
                    $('#number-reply-' + param).text("");
                }
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}
function existFormReplyQuestion(param) {
    $('#reply-content-form' + param).trigger("reset");
    $('#reply-form' + param).css("display", "none")
}

function viewMoreReply(param) {
    var viewDiv = '#list-reply-content-' + param;
    var seeLessDiv = '#see-less-reply-' + param;
    var viewMoreDiv = '#view-more-reply-' + param;
    var loadingDiv = '#loading-reply-' + param;
    $(viewMoreDiv).css("display", "none");
    $(loadingDiv).css("display", "block");
    $.ajax({
        type: 'GET',
        url: '/api/lecturer/answers/get/' + param,
        dataType: 'json',
        success: function (data) {
            $(viewDiv).html("");
            $("#new-reply-content").val("");
            var html = "";
            for (let i = 0; i < data.length; i++) {
                if (data[i].studentName == null) {
                    // content
                    html = "<div class=\"reply-content border-bottom\" id=\"" + data[i].answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].lecturerName + "(You)</span></h6>\n" +
                        "                     <p class=\"lec__question-content\"  id=\"reply-content-"+ data[i].answerId +"\">" + data[i].answerContent + "</p>";

                    // edit section
                    html+="<div class=\"edit-reply-div\" id=\"update-reply"+ data[i].answerId+ "\" style=\"display: none\">\n" +
                        "                                                    <label id=\"update-reply-error"+ data[i].answerId+"\" class=\"display-none\">Please enter something to update.</label>\n" +
                        "                                                    <textarea class=\"update-reply\" id=\"update-reply-content-"+ data[i].answerId +"\"></textarea>\n" +
                        "                                                    <button id=\"close-update-reply-" + data[i].answerId+ "\" type=\"button\" title=\"exist\"\n" +
                        "                                                            reply-id=\""+ data[i].answerId+"\" onclick=existFormEditReply(\"" +data[i].answerId+"\")\n" +
                        "                                                            class=\"exist-form-edit-reply btn-danger\"><i class=\"fa-solid fa-xmark\"></i> Close</button> " +
                        "                                                            <a type=\"button\" class=\"display-none\" title=\"Sending\" id=\"sending-update-reply"+ data[i].answerId+ "\"><i\n" +
                        "                                                            class=\"fas fa-spinner fa-spin\"></i> Sending...</a>\n" +
                        "                                                    <button type=\"button\" title=\"Edit\" id=\"send-update-reply-"+ data[i].answerId+"\" reply-id=\"" + data[i].answerId + "\"\n" +
                        "                                                            onclick=submitFormEditReply(\"" + data[i].answerId + "\") class=\"btn-save\">" +
                        "                                                    <i class=\"fa-solid fa-paper-plane\"></i> Edit\n" +
                        "                                                    </button>\n" +
                        "                                                    </div>";
                    // date and link
                    html+="<p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].createdDate + "</span> " +
                        "                     <a class=\"lec__edit-reply view-reply-link-item edit-reply\" reply-id=\"" + data[i].answerId + "\">Edit</a> | " +
                        "                     <a class=\"lec__delete-reply view-reply-link-item delete-reply\" reply-id=\"" + data[i].answerId + "\" onclick=deleteReply(\"" + data[i].answerId + "\",\"" + data[i].questionId + "\")>Delete</a>\n" +
                        "                     </div>";
                } else {
                    html = "<div class=\"reply-content  border-bottom\"  id=\"" + data[i].answerId + "\">\n" +
                        "                     <h6 class=\"lec__question-creater-name\"><i class=\"fa-solid fa-user\"></i> <span>" + data[i].studentName + "</span></h6>\n" +
                        "                     <p class=\"lec__question-content\">" + data[i].answerContent + "</p>\n" +
                        "                     <p class=\"lec__question-content\" ><span class=\"lec__answer-date\" >" + data[i].createdDate + "</span> " +
                        "                     </div>";
                }
                $(viewDiv).append(html);
                let modified_val = data[i].answerContent .replace(/"/g, '&quot;')
                $('#update-reply-content-' + data[i].answerId).val(modified_val);
            }
            $(loadingDiv).css("display", "none");
            // $(viewDiv).html(html);
            $(seeLessDiv).css("display", "block");
        },
        error: function (xhr) {
            // Handle errors
        }
    });
}


function submitFormEditReply(param) {
    var content = $('#update-reply-content-' + param).val();
    var trimmedString = $.trim(content);
    if (trimmedString == '') {
        $('#update-reply-error' + param).addClass('error');
    } else {
        $('#update-reply-error' + param).css("display", "none");
        $('#sending-update-reply' + param).css("display", "inline");
        $("#send-update-reply-" + param).css("display", 'none');
        $('#close-update-reply-' + param).css("display", "none");
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/my_question/replies/' + param + '/update',
            data: {'answerContent': content},
            dataType: 'json',
            success: function (data) {
                $("#update-reply" + param).css("display", "none");
                $("#update-reply-content-" + param).val(data.answerContent);
                $("#reply-content-" + param).html(data.answerContent);
                $("#reply-content-" + param).css("display", "block");
                $('#sending-update-reply' + param).css("display", "none");
                $("#send-update-reply-" + param).css("display", 'inline');
                $('#close-update-reply-' + param).css("display", "inline");
            },
            error: function (xhr) {
                // Handle errors
            }
        });
    }
}

function existFormEditReply(param) {
    console.log(param);
    $("#update-reply" + param).css("display", "none");
    $("#reply-content-" + param).css("display", "block");
    $('#update-reply-error' + param).removeClass('error')
}

function deleteReply(param, param2) {
    console.log(param)
    var result = window.confirm("Do you want to delete your reply?");
    if (result) {
        $.ajax({
            type: 'POST',
            url: '/api/lecturer/my_question/replies/' + param + '/delete',
            success: function (data) {
                console.log('success-delete-reply' + param);
                $("#" + param).html("");
                $("#" + param).css("display", "none");
                // change total of answer
                var totalReply = $('#number-reply-' + param2).text();
                var intValue = parseInt(totalReply);

                if (!isNaN(intValue)) {
                    // Increment the integer by 1
                    var newValue = intValue - 1;

                    // Set the new value as the text of the span
                    $('#number-reply-' + param2).text(newValue);
                } else {
                    $('#number-reply-' + param2).text("");
                }
            },
            error: function (xhr) {
                console.log('error-delete-reply')
            }
        });
    }
}
function seeLessReply(param) {
    var viewDiv = '#list-reply-content-' + param;
    var seeLessDiv = '#see-less-reply-' + param;
    var viewMoreDiv = '#view-more-reply-' + param;
    $(viewMoreDiv).css("display", "block");
    $(seeLessDiv).css("display", "none");
    $(viewDiv).html('');
}

function showReplyForm(questionId) {
    var questionIdParam = questionId;
    console.log(questionId);
    if (questionId == null) {
        questionIdParam = this.getAttribute('question-id');
    }
    $("#reply-form" + questionIdParam).css("display", "block");
}

$(document).ready(function () {
    // add scroll to fragment identifier if id exist in URL
    var hash = window.location.hash;
    var sectionId = hash.substring(1);// exclude '#'
    if ($("#" + sectionId).length > 0) {
        viewMoreReply(sectionId);
    }
    // var element = document.querySelector("#" + sectionId);
    $("#"+ sectionId).get(0).scrollIntoView();

    // edit reply
    $("body").on("click", ".edit-reply", function () {
        // $(this).next(".edit-question-div").toggle();
        var replyId = $(this).attr("reply-id");
        console.log("update-reply"+ replyId)
        $("#update-reply" + replyId).css("display", "block");
        $("#reply-content-" + replyId).css("display", "none");
    })
})

const addSupportFile = document.getElementById('addSupportFile');
const updateSupportingFiles = document.getElementById('updateSupportingFiles');
const saveUpdateFilesWrap = document.getElementById('saveUpdateFilesWrap');
const saveUpdateFiles = document.getElementById('saveUpdateFiles');
const backUpdateFile = document.getElementById('backUpdateFile');
const newSupportFileInput = document.getElementById('newSupportFileInput');
const noSupportingFile = document.getElementById('noSupportingFile');
const saveWithLoading = document.getElementById('saveWithLoading');
const saveWithoutLoading = document.getElementById('saveWithoutLoading');

const removeFiles = document.getElementsByClassName('remove-file');
const supportingFileDownload = document.getElementsByClassName('supporting-file-download');
const loadingAnimation = document.querySelector('.loading-animation');

const listSupportingFiles = [];

const supportingFileElements = document.getElementsByClassName('supporting-file-download');
for (let i = 0; i < supportingFileElements.length; i++) {
    const fileName = supportingFileElements[i].getAttribute('file-name');
    listSupportingFiles.push(fileName);
}

console.log(listSupportingFiles);

newSupportFileInput.addEventListener('change', handleFileSelection);

function handleFileSelection() {
    const selectedFiles = newSupportFileInput.files;
    const totalFiles = selectedFiles.length + listSupportingFiles.length;

    if (totalFiles > 3) {
        alert("Total number of files exceeds the limit of 3");
        // Reset the file input by clearing the selected files
        newSupportFileInput.value = null;
    }
}

function saveFiles() {
    if(listSupportingFiles.length == supportingFileElements.length && supportingFileElements == null){
        return;
    }

    saveWithoutLoading.style.display = 'none';
    saveWithLoading.style.display = 'inline';

    const formData = new FormData();

    const documentId = document.getElementById('documentId').value;

    // Thêm các tệp đã tải lên vào formData
    const selectedFiles = newSupportFileInput.files;
    for (let i = 0; i < selectedFiles.length; i++) {
        formData.append('files', selectedFiles[i]);
    }

    // Thêm các phần tử trong listSupportingFiles vào formData
    for (let i = 0; i < listSupportingFiles.length; i++) {
        formData.append('supportingFiles', listSupportingFiles[i]);
    }

    // Gọi AJAX để gửi formData lên phía máy chủ
    $.ajax({
        url: '/lecturer/' + documentId + '/update_supporting_files',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function() {
            window.location.reload();
        },
        error: function(error) {
            // Xử lý lỗi từ máy chủ
            console.log(error);
        }
    });
}
function backUpdateFiles() {
    window.location.reload();
}
function updateFiles() {

    addSupportFile.style.display = 'block';
    updateSupportingFiles.style.display = 'none';
    if(noSupportingFile != null){
        noSupportingFile.style.display = 'none';
    }

    saveUpdateFilesWrap.style.display = 'inline-block';
    saveUpdateFiles.style.display = 'inline';
    backUpdateFile.style.display = 'inline';

    for (let i = 0; i < removeFiles.length; i++) {
        const removeFile = removeFiles[i];
        removeFile.style.display = 'inline'; // Thiết lập thuộc tính "display" thành "block"
    }
    for (let i = 0; i < supportingFileDownload.length; i++) {
        let supportingFile = supportingFileDownload[i];
        console.log(supportingFileDownload[i]);
        supportingFile.removeAttribute('onclick');
        supportingFile.classList.add('supporting-file-when-updating');
    }
}
function removeFile(element) {

    const fileName = element.getAttribute('file-name');
    const index = listSupportingFiles.indexOf(fileName);
    if (index > -1) {
        listSupportingFiles.splice(index, 1);
    }

    // Xóa phần tử khỏi DOM
    element.remove();

    console.log(listSupportingFiles);
}

function addFileInput() {
    newSupportFileInput.style.display = 'block';
}
function downloadFile(fileName) {
    $.ajax({
        url: '/api/lecturer/download',
        method: 'GET',
        data: {fileName: fileName},
        xhrFields: {
            responseType: 'blob'
        },
        success: function (data) {
            var downloadLink = document.createElement('a');
            downloadLink.href = URL.createObjectURL(data);
            downloadLink.download = fileName;
            document.body.appendChild(downloadLink);
            downloadLink.click();
        },
        error: function () {
            console.log("Error when download file");
        }
    });
}
