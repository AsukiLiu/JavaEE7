window.onload = function() {
    document.getElementById('getPerson').addEventListener(
            'click',
            function(event) {
                location.href = document.forms[0].action + '/'
                        + document.getElementById('personId').value;
            }, false);
};
