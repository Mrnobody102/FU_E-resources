// FILE VALIDATIONS
let fileInput = document.getElementById('fileUploadInput');
let cancelButton = document.getElementById('cancelUploadButton');

let file = null;
let isUploading = false;

const allowedFormats = ['pdf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx',
    'md', 'html', 'txt', 'm4a', 'flac', 'mp3', 'wav', 'wma', 'aac',
    'mp4', 'mov', 'avi', 'flv', 'mkv', 'webm',
    'jpg', 'jpeg', 'gif', 'png', 'svg',
    'exe', 'psd', 'eps', 'jar', 'zip', 'rar'];

cancelButton.addEventListener('click', function () {
    if (isUploading) {
        isUploading = false;
        file = null;
        fileInput.value = '';
        document.getElementById('previewContainer').innerHTML = 'File will be previewed here.';
    } else {
        console.log('Không có tệp tin đang được tải lên để hủy');
    }
});

fileInput.addEventListener('change', function (event) {
    isUploading = true;
    file = event.target.files[0];
    if(file == null) {
        isUploading = false;
        fileInput.value = '';
        document.getElementById('previewContainer').innerHTML = 'File will be previewed here.';
    }
    var fileSizeInMB = file.size / (1024 * 1024); // Chuyển đổi kích thước tệp tin thành Megabyte
    if (fileSizeInMB > 100) {
        Swal.fire(
            'File size exceeds 100MB!',
            'Please choose a file with a capacity of less than 100MB.',
            'error'
        );
        event.target.value = ''; // Xóa giá trị của phần tử tải lên để ngăn người dùng tải lên tệp tin vượt quá kích thước
        return;
    }

    let fileName = file.name;

    let fileExtension = getFileExtension(fileName.toLowerCase());

    if (allowedFormats.includes(fileExtension)) {
        document.getElementById('previewContainer').innerHTML = '';
        if (fileSizeInMB < 20) {
            let reader = new FileReader();
            reader.onload = function (e) {
                let preview;
                if (file.type.startsWith('image/')) {
                    preview = document.createElement('img');
                    preview.src = e.target.result;
                    preview.style.maxWidth = '100%';
                    preview.style.maxHeight = '400px';
                } else if (file.type.startsWith('video/')) {
                    preview = document.createElement('video');
                    preview.src = e.target.result;
                    preview.controls = true;
                    preview.style.maxWidth = '100%';
                } else if (file.type.startsWith('audio/')) {
                    preview = document.createElement('audio');
                    preview.src = e.target.result;
                    preview.controls = true;
                } else if (file.type === 'application/pdf') {
                    if (fileSizeInMB < 1) {
                        preview = document.createElement('embed');
                        preview.src = e.target.result;
                        preview.width = '100%';
                        preview.height = '600px';
                    } else {
                        preview = document.createElement('p');
                        preview.textContent = 'PDF file larger than 1MB will not be previewed.';
                    }
                } else {
                    preview = document.createElement('span');
                    preview.textContent = 'This file cannot be previewed.';
                }
                document.getElementById('previewContainer').appendChild(preview);
            }
            reader.readAsDataURL(file);
        } else {
            document.getElementById('previewContainer').innerHTML = 'Only preview files smaller than 10MB.';
        }
    } else {
        Swal.fire(
            'File format not supported!',
            'Please choose another format.',
            'error'
        );
        event.target.value = ''; // Xóa giá trị của phần tử tải lên để ngăn người dùng tải lên tệp tin không hợp lệ
        return;
    }
});

function getFileExtension(filename) {
    return filename.split('.').pop();
}