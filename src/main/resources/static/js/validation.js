
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