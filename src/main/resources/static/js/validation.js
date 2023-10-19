function validate_addCourse() {
    $('#add-form').validate({
        rules: {
            courseCode: {
                required: true,
                minlength: 3,
                maxlength: 20 // Updated for realistic limits
            },
            courseName: {
                required: true,
                minlength: 5,
                maxlength: 150
            },
            description: {
                maxlength: 1500 // Increased character limit for descriptions
            },
            lesson: {
                required: true,
                digits: true,
                min: 1 // Lessons should be a positive number
            },
        },
        messages: {
            courseCode: {
                required: "Course code is required!",
                minlength: "Course code must have at least 3 characters!",
                maxlength: "Course code can have at most 20 characters!"
            },
            courseName: {
                required: "Course name is required!",
                minlength: "Course name must have at least 5 characters!",
                maxlength: "Course name can have at most 150 characters!"
            },
            description: {
                maxlength: "Description can have at most 1500 characters!"
            },
            lesson: {
                required: "Number of lessons is required!",
                digits: "Please enter a valid number of lessons",
                min: "Number of lessons must be at least 1"
            },
        }
    });
}

function validate_updateCourse() {
    $('#update-form').validate({
        rules: {
            courseCode: {
                required: true,
                minlength: 3,
                maxlength: 20 // Updated for realistic limits
            },
            courseName: {
                required: true,
                minlength: 5,
                maxlength: 150
            },
            description: {
                maxlength: 1500 // Increased character limit for descriptions
            },
            lesson: {
                required: true,
                digits: true,
                min: 1 // Lessons should be a positive number
            },
        },
        messages: {
            courseCode: {
                required: "Course code is required!",
                minlength: "Course code must have at least 3 characters!",
                maxlength: "Course code can have at most 20 characters!"
            },
            courseName: {
                required: "Course name is required!",
                minlength: "Course name must have at least 5 characters!",
                maxlength: "Course name can have at most 150 characters!"
            },
            description: {
                maxlength: "Description can have at most 1500 characters!"
            },
            lesson: {
                required: "Number of lessons is required!",
                digits: "Please enter a valid number of lessons",
                min: "Number of lessons must be at least 1"
            },
        }
    });
}
function validate_addAccount() {
    $('#add-account').validate({
        rules: {
            username: {
                required: true,
                minlength: 5, // Slightly longer username for security
                maxlength: 50
            },
            password: {
                required: true,
                minlength: 8, // Stronger password requirement
                maxlength: 30
            },
            email: {
                required: true,
                email: true,
                maxlength: 100
            },
            name: {
                required: true,
                minlength: 2,
                maxlength: 50
            },
            dateOfBirth: {
                required: true,
                date: true
            },
            campus: {
                required: true
            },
            role: {
                required: true
            },
        },
        messages: {
            username: {
                required: "Username is required!",
                minlength: "Username must have at least 5 characters!",
                maxlength: "Username can have at most 50 characters!"
            },
            password: {
                required: "Password is required!",
                minlength: "Password must have at least 8 characters!",
                maxlength: "Password can have at most 30 characters!"
            },
            email: {
                required: "Email is required!",
                email: "Please enter a valid email address!",
                maxlength: "Email can have at most 100 characters!"
            },
            name: {
                required: "Full Name is required!",
                minlength: "Full Name must have at least 2 characters!",
                maxlength: "Full Name can have at most 50 characters!"
            },
            dateOfBirth: {
                required: "Date of Birth is required!",
                date: "Please enter a valid date (YYYY-MM-DD format)!"
            },
            campus: {
                required: "Campus is required!"
            },
            role: {
                required: "Role is required!"
            }
        }
    });
}

function validate_updateAccount() {
    $('#update-account').validate({
        rules: {
            username: {
                required: true,
                minlength: 5, // Slightly longer username for security
                maxlength: 50
            },
            password: {
                minlength: 8, // Stronger password requirement (optional)
                maxlength: 30
            },
            email: {
                required: true,
                email: true,
                maxlength: 100
            },
            name: {
                required: true,
                minlength: 2,
                maxlength: 50
            },
            dateOfBirth: {
                required: true,
                date: true
            },
            campus: {
                required: true
            },
            role: {
                required: true
            },
        },
        messages: {
            username: {
                required: "Username is required!",
                minlength: "Username must have at least 5 characters!",
                maxlength: "Username can have at most 50 characters!"
            },
            password: {
                minlength: "Password must have at least 8 characters (optional)",
                maxlength: "Password can have at most 30 characters"
            },
            email: {
                required: "Email is required!",
                email: "Please enter a valid email address!",
                maxlength: "Email can have at most 100 characters!"
            },
            name: {
                required: "Full Name is required!",
                minlength: "Full Name must have at least 2 characters!",
                maxlength: "Full Name can have at most 50 characters!"
            },
            dateOfBirth: {
                required: "Date of Birth is required!",
                date: "Please enter a valid date (YYYY-MM-DD format)!"
            },
            campus: {
                required: "Campus is required!"
            },
            role: {
                required: "Role is required!"
            }
        }
    });
}
