window.onload = function() {
    var errorDiv = document.querySelector('[data-error]');
    if (errorDiv && errorDiv.getAttribute('data-error')) {
        alert(errorDiv.getAttribute('data-error'));
    }
}
