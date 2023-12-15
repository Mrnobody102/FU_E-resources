// FILE VALIDATIONS
let fileInput = document.getElementById('fileUploadInput');
let cancelButton = document.getElementById('cancelUploadButton');

let file = null;
let isUploading = false;

const allowedFileUploadNumber = 3;

let supportFileInput = document.getElementById('supportFileInput');
let cancelSupportFileButton = document.getElementById('cancelSupportFileButton');

let files = null;
let isSupportFileUploading = false;

const allowedFormats = ['pdf', 'doc', 'docx', 'ppt', 'pptx', 'java',
    'md', 'html', 'txt', 'm4a', 'flac',
    'mp3', 'wav', 'wma', 'aac', 'ogg',
    'mp4', 'mov', 'avi', 'flv', 'mkv', 'webm',
    'jpg', 'jpeg', 'gif', 'png', 'svg'];

const supportFileFormats = ['pdf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx',
    'md', 'html', 'txt', 'm4a', 'flac', 'mp3', 'wav', 'wma', 'aac',
    'mp4', 'mov', 'avi', 'flv', 'mkv', 'webm',
    'jpg', 'jpeg', 'gif', 'png', 'svg',
    'exe', 'psd', 'eps', 'jar', 'zip', 'rar',
    'java', 'c', 'cpp', 'cc', 'cxx', 'cs', 'py', 'js', 'rb', 'swift', 'go', 'php', 'css', 'ts',
    'ai', 'indd', 'prproj', 'aep', 'xd', 'fla', 'aup', 'puppet', 'dn', 'muse', 'abr'];

cancelButton.addEventListener('click', function () {
    if (isUploading) {
        isUploading = false;
        file = null;
        fileInput.value = '';
        document.getElementById('previewContainer').innerHTML = '<i>File will be previewed here.</i>\n' +
            '                                            <br>\n' +
            '                                            <br>\n' +
            '                                            <span>File formats that allow display as content:</span>\n' +
            '                                            <br>\n' +
            '                                            <i>\n' +
            '                                                <b>- Document:</b> .pdf, .doc, .docx, .ppt, .pptx, .txt, .html, .md\n' +
            '                                                <br>\n' +
            '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
            '                                                <br>\n' +
            '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
            '                                                <br>\n' +
            '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
            '                                                <br>\n' +
            '                                            </i>\n' +
            '\n' +
            '                                            <br>\n' +
            '\n' +
            '                                            <span>Previewable file formats:</span>\n' +
            '                                            <i>\n' +
            '                                                <br>\n' +
            '                                                <b>- Document:</b> .pdf, .txt, .html, .md\n' +
            '                                                <br>\n' +
            '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
            '                                                <br>\n' +
            '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
            '                                                <br>\n' +
            '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
            '                                            </i>';
    } else {
        console.log('Không có tệp tin đang được tải lên để hủy');
    }
});

cancelSupportFileButton.addEventListener('click', function () {
    if (isSupportFileUploading) {
        isSupportFileUploading = false;
        files = null;
        supportFileInput.value = '';
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
        document.getElementById('previewContainer').innerHTML = '<i>File will be previewed here.</i>\n' +
            '                                            <br>\n' +
            '                                            <br>\n' +
            '                                            <span>File formats that allow display as content:</span>\n' +
            '                                            <br>\n' +
            '                                            <i>\n' +
            '                                                <b>- Document:</b> .pdf, .doc, .docx, .ppt, .pptx, .txt, .html, .md\n' +
            '                                                <br>\n' +
            '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
            '                                                <br>\n' +
            '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
            '                                                <br>\n' +
            '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
            '                                                <br>\n' +
            '                                            </i>\n' +
            '\n' +
            '                                            <br>\n' +
            '\n' +
            '                                            <span>Previewable file formats:</span>\n' +
            '                                            <i>\n' +
            '                                                <br>\n' +
            '                                                <b>- Document:</b> .pdf, .txt, .html, .md\n' +
            '                                                <br>\n' +
            '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
            '                                                <br>\n' +
            '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
            '                                                <br>\n' +
            '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
            '                                            </i>';
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
                } else if (file.type === 'application/pdf' || file.type.startsWith('text')) {
                    if (fileSizeInMB < 1) {
                        preview = document.createElement('embed');
                        preview.src = e.target.result;
                        preview.width = '100%';
                        preview.height = '600px';
                    } else {
                        preview = document.createElement('p');
                        preview.textContent = 'Document file larger than 1MB will not be previewed.';
                    }
                } else {
                    preview = document.createElement('span');
                    preview.innerHTML = '<b>This file format is not support to preview.</b>\n' +
                        '                                            <br>\n' +
                        '                                            <br>\n' +
                        '                                            <span>File formats that allow display as content:</span>\n' +
                        '                                            <br>\n' +
                        '                                            <i>\n' +
                        '                                                <b>- Document:</b> .pdf, .doc, .docx, .ppt, .pptx, .txt, .html, .md\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
                        '                                                <br>\n' +
                        '                                            </i>\n' +
                        '\n' +
                        '                                            <br>\n' +
                        '\n' +
                        '                                            <span>Previewable file formats:</span>\n' +
                        '                                            <i>\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Document:</b> .pdf, .txt, .html, .md\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Image:</b> .jpg, .jpeg, .gif, .png, .svg\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Video:</b> .mp4, .webm, .mov, .avi, .flv, .mkv\n' +
                        '                                                <br>\n' +
                        '                                                <b>- Audio:</b> .mp3, .flac, .aac, .wav\n' +
                        '                                            </i>';
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

supportFileInput.addEventListener('change', function (event) {
    isSupportFileUploading = true;
    files = event.target.files;
    if(files.length === 0) {
        isSupportFileUploading = false;
        supportFileInput.value = '';
    }
    if(files.length > allowedFileUploadNumber){
        Swal.fire(
            'Only allowed to upload a maximum of 3 files!',
            'Please choose no more than 3 files.',
            'error'
        );
        supportFileInput.value = '';
    }
    for (let i = 0; i < allowedFileUploadNumber; i++) {
        if (files[i].size / (1024 * 1024) > 50) {
            Swal.fire(
                'Each supporting file size not exceeds 50MB!',
                'Please choose a file with a capacity of less than 50MB.',
                'error'
            );
            event.target.value = ''; // Xóa giá trị của phần tử tải lên để ngăn người dùng tải lên tệp tin vượt quá kích thước
            return;
        }

        let supportFileName = files[i].name;

        let spFileExtension = getFileExtension(supportFileName.toLowerCase());

        if (!supportFileFormats.includes(spFileExtension)) {
            Swal.fire(
                'File format not supported!',
                'Please choose another format.',
                'error'
            );
            event.target.value = ''; // Xóa giá trị của phần tử tải lên để ngăn người dùng tải lên tệp tin không hợp lệ
            return;
        }
    }

});

function getFileExtension(filename) {
    return filename.split('.').pop();
}