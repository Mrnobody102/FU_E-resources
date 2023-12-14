// VALIDATE COURSE
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

// VALIDATE ACCOUNT
function validate_addAccount() {
    $('#add-account').validate({
        rules: {
            username: {
                required: true,
                minlength: 5, // Slightly longer username for security
                maxlength: 50
            },
            password: {
                // required: true,
                // minlength: 8, // Stronger password requirement
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

function validateAddLecturerForm() {
    $("#addLecturerForm").validate({
        rules: {
            lecturerEmail: {
                required: true,
                email: true
            }
        },
        messages: {
            lecturerEmail: {
                required: "Please enter the lecturer's email.",
                email: "Please enter a valid email address."
            }
        },
        errorElement: 'div',
        errorPlacement: function (error, element) {
            error.addClass('invalid-feedback');
            error.insertAfter(element);
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('is-invalid').removeClass('is-valid');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass('is-invalid').addClass('is-valid');
        }
    });
}

function validateAddTrainingTypeForm() {
    $("#add-trainingType").validate({
        rules: {
            // Rules for the training type name
            trainingTypeName: {
                required: true,
                minlength: 2
            },
            // Rules for the training type description
            trainingTypeDescription: {
                required: true,
                minlength: 10
            }
        },
        messages: {
            trainingTypeName: {
                required: "Please enter the training type name.",
                minlength: "The training type name must be at least 2 characters long."
            },
            trainingTypeDescription: {
                required: "Please enter a description for the training type.",
                minlength: "The description must be at least 10 characters long."
            }
        }
    });
}
function validateUpdateTrainingType() {
    $("#update-trainingType").validate({
        rules: {
            // Validation rules for the training type name
            trainingTypeName: {
                required: true,
                minlength: 2
            },
            // Validation rules for the training type description
            trainingTypeDescription: {
                required: true,
                minlength: 10
            }
        },
        messages: {
            trainingTypeName: {
                required: "Please enter the training type name.",
                minlength: "The training type name must be at least 2 characters long."
            },
            trainingTypeDescription: {
                required: "Please enter a description for the training type.",
                minlength: "The description must be at least 10 characters long."
            }
        }
    });
}
function validate_updateTopic() {
    $("#update-topic").validate({
        rules: {
            topicTitle: {
                required: true,
                minlength: 3,
                maxlength: 50
            },
            topicDescription: {
                required: true,
                minlength: 5
            }
        },
        messages: {
            topicTitle: {
                required: "Please enter a topic title",
                minlength: "Topic title must be at least 3 characters!",
                maxlength: "Topic Title can have at most 50 characters!"
            },
            topicDescription: {
                required: "Please enter a topic description",
                minlength: "Topic description must be at least 5 characters!"
            }
        }
    });
}

function validate_addTopic() {
    $('#add-topic').validate({
        rules: {
            topicTitle: {
                required: true,
                minlength: 3, // Change this as needed
                maxlength: 50  // Change this as needed
            },
            topicDescription: {
                minlength: 5,// Change this as needed
                required: true,
            }
        },
        messages: {
            topicTitle: {
                required: "Topic Title is required!",
                minlength: "Topic Title must have at least 3 characters!", // Change this as needed
                maxlength: "Topic Title can have at most 50 characters!"  // Change this as needed
            },
            topicDescription: {
                required: "Please enter a topic description",
                minlength: "Topic Description can have at least 5 characters!" // Change this as needed
            }
        }
    });
}
function validate_addResourceType() {
    $("#add-resource-type").validate({
        rules: {
            resourceTypeName: {
                required: true,
                minlength: 3 // Example: minimum length of 3 characters
            }
        },
        messages: {
            resourceTypeName: {
                required: "Please enter a resource type name",
                minlength: "Resource type name must be at least 3 characters long"
            }
        }
    });
}

function validate_updateresourceType() {
    $("#update-resource-type").validate({
        rules: {
            resourceTypeName: {
                required: true,
                minlength: 3 // Example: minimum length of 3 characters
            }
        },
        messages: {
            resourceTypeName: {
                required: "Please enter a resource type name",
                minlength: "Resource type name must be at least 3 characters long"
            }
        }
    });
}



// VALIDATE DOCUMENTS
function validate_addDocument(){
    $("#add-document").validate({
        rules: {
            title: "required",
            description: {
                required: true,
                minlength: 10
            },

        },
        messages: {
            title: "Please enter a title",
            description: {
                required: "Please provide a description",
                minlength: "Your description must be at least 10 characters long"
            }
        },

    });
}

function validate_updateDocument(){
    $("#update-document").validate({
        rules: {
            title: "required",
            description: {
                required: true,
                minlength: 10
            },

        },
        messages: {
            title: "Please enter a title",
            description: {
                required: "Please provide a description",
                minlength: "Your description must be at least 10 characters long"
            }
        },

    });
}
