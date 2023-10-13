
function check_submit(){
    var code=  document.getElementById("subjectCode");
    var name=  document.getElementById("subjectName");
    if(code.value!=""){
        if(code.value.length>6){
            alert("Please enter less than 6 characters")
            code.focus();
            return false;
        }
    }else{
        alert("Please enter Subject Code")
        code.focus();
        return false;
    }

    if(name.value==""){
        alert("Please enter Subject Name")
        name.focus();
        return false;
    }
}

function validate_addCourse() {
    $('#add-form').validate({
        rules: {
            courseCode: {
                required: true,
                minlength: 2,
                maxlength: 50
            },
            courseName: {
                required: true,
                minlength: 3,
                maxlength: 30
            },
            description: {
                maxlength: 200
            },
            lesson: {
                required: true,
                digits: true
            },
        },
        messages: {
            courseCode: {
                required: "Course code is required!",
                minlength: "Course code must have at least 2 characters!",
                maxlength: "Course code can have at most 50 characters!"
            },
            courseName: {
                required: "Course name is required!",
                minlength: "Course name must have at least 3 characters!",
                maxlength: "Course name can have at most 30 characters!"
            },
            description: {
                maxlength: "Description can have at most 200 characters!" // Thay đổi thông báo lỗi cho description
            },
            lesson: {
                required: "Number of lessons is required!",
                digits: "Please enter a valid number of lessons" // Thông báo khi không nhập số nguyên
            },
        }
    });
}


function validate_updateCourse() {
    $('#update-form').validate({
        rules: {
            courseCode: {
                required: true,
                minlength: 2,
                maxlength: 50
            },
            courseName: {
                required: true,
                minlength: 3,
                maxlength: 30
            },
            description: {
                maxlength: 200
            },
            lesson: {
                required: true,
                digits: true
            },
        },
        messages: {
            courseCode: {
                required: "Course code is required!",
                minlength: "Course code must have at least 2 characters!",
                maxlength: "Course code can have at most 50 characters!"
            },
            courseName: {
                required: "Course name is required!",
                minlength: "Course name must have at least 3 characters!",
                maxlength: "Course name can have at most 30 characters!"
            },
            description: {
                maxlength: "Description can have at most 200 characters!" // Thay đổi thông báo lỗi cho description
            },
            lesson: {
                required: "Number of lessons is required!",
                digits: "Please enter a valid number of lessons" // Thông báo khi không nhập số nguyên
            },
        }
    });
}

