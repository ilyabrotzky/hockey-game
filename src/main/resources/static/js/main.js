$(function() {
	$('[data-toggle="tooltip"]').tooltip();
	$('[rel="tooltip"]').tooltip();
});

function showLoading() {
	
};

function hideLoading() {
	
};

// WEBSOCKET
var attentionClient = null;
var ctxAttention = "/hockey/home";

$(document).ready(function() {
	connectAttention();
});

function connectAttention() {
	var socket = new SockJS(ctxAttention);
	attentionClient = Stomp.over(socket);
	attentionClient.connect({}, function(frame) {

		attentionClient.subscribe('/attention-client', function(msg) {
			addAttention(JSON.parse(msg.body));
		});
	});
};

function disconnectAttention() {
	if (atendimentoClient != null) {
		atendimentoClient.disconnect();
	}
};

// MODALS
var idAttention;
var accept;
$('#modal-accept-attention').on('show.bs.modal', function(event) {
	idAttention = $(event.relatedTarget).data("id");
	accept = $(event.relatedTarget).data("accept");

	var bodySpanData = "<p>" + $(event.relatedTarget).data("accept-warning") + "</p>";
	
	if ($(event.relatedTarget).data("type") == "MESSAGE") {
		bodySpanData += "<p><strong>Message: </strong><i>\"" + $(event.relatedTarget).data("message") + "\"</i></p>";
	}

	$(this).find('.modal-body span').html(bodySpanData);
});

// AJAX
var ctxHome = "/hockey/home";
function acceptAttention() {
	var response = $.ajax({
		url : ctxHome + "/accept-attention/" + idAttention + "/" + accept,
		type : 'POST',
		contentType : 'application/json; charset=utf-8',
		dataType : 'text json',
		async: false,
		beforeSend: function(){ showLoading(); },
		complete: function(){ hideLoading(); }
	});

	response.done(function(e) {
		$("#attention-" + idAttention).hide('slow', function(){ $("#attention-" + idAttention).remove(); });
		$('#modal-accept-attention').modal('hide');
	});

	response.fail(function() {
		$.growl.error({title: "Error", message: "It would not possible to complete your request", location: "tr" });
		$('#modal-accept-attention').modal('hide');
	});
};