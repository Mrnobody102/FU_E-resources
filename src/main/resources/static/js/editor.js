// CK editor
ClassicEditor
    .create(document.querySelector('#editor'), {
        ckfinder: {
            uploadUrl: '/api/lecturer/upload_image_editor',
            withCredentials: true
        },
    })
    .then(editor => {
        editor.editing.view.change(writer => {
            writer.setStyle('height', '80vh', editor.editing.view.document.getRoot());
        });
        console.log(Array.from(editor.ui.componentFactory.names()));
    })
    .catch(error => {
        console.error(error);
    });