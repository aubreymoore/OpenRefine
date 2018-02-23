var ManageAccountDialog = {};
/*
  $.post(
      "command/core/get-all-preferences",
      null,
      populatePreferences,
      "json"
  );
*/

ManageAccountDialog.launch = function(logged_in_username, callback) {
   $.post(
      "command/core/get-all-preferences",
      null,
      function (preferences) {
         ManageAccountDialog.display(logged_in_username, preferences.wikidata_credentials, callback);
      },
      "json"
  );
};

ManageAccountDialog.display = function(logged_in_username, saved_credentials, callback) {
  var self = this;
  var frame = $(DOM.loadHTML("wikidata", "scripts/dialogs/manage-account-dialog.html"));
  var elmts = this._elmts = DOM.bind(frame);
  console.log(saved_credentials);

  this._elmts.dialogHeader.text($.i18n._('wikidata-account')["dialog-header"]);
  this._elmts.explainLogIn.text($.i18n._('wikidata-account')["explain-log-in"]);
  this._elmts.usernameLabel.text($.i18n._('wikidata-account')["username-label"]);
  this._elmts.usernameInput.attr("placeholder", $.i18n._('wikidata-account')["username-placeholder"]);
  this._elmts.passwordLabel.text($.i18n._('wikidata-account')["password-label"]);
  this._elmts.passwordInput.attr("placeholder", $.i18n._('wikidata-account')["password-placeholder"]);
  this._elmts.rememberCredentialsLabel.text($.i18n._('wikidata-account')["remember-credentials-label"]);
  this._elmts.dialogHeader.text($.i18n._('wikidata-account')["dialog-header"]);
  this._elmts.cancelButton1.text($.i18n._('wikidata-account')["close"]);
  this._elmts.cancelButton2.text($.i18n._('wikidata-account')["close"]);
  this._elmts.loggedInAs.text($.i18n._('wikidata-account')["logged-in-as"]);
  this._elmts.logoutButton.text($.i18n._('wikidata-account')["log-out"]);
  this._elmts.loginButton.text($.i18n._('wikidata-account')["log-in"]);

  this._level = DialogSystem.showDialog(frame);

  var dismiss = function() {
    DialogSystem.dismissUntil(self._level - 1);
  };

  if (logged_in_username != null) {
      elmts.loginArea.hide();
  } else {
      elmts.logoutArea.hide();
  }

  elmts.loggedInUsername.text(logged_in_username);
  
  frame.find('.cancel-button').click(function() {
     dismiss();
     callback(null);
  });

  elmts.loginButton.click(function() {
    frame.hide();
    $.post(
       "command/wikidata/login",
       elmts.loginForm.serialize(),
       function(data) {
         if (data.logged_in) {
           dismiss();
           callback(data.username);
         } else {
            frame.show();
            elmts.invalidCredentials.text("Invalid credentials.");
         }
       });
  });

  elmts.logoutButton.click(function() {
    $.post(
       "command/wikidata/login",
       "logout=true",
       function(data) {
         if (!data.logged_in) {
           dismiss();
           callback(null);
         }
    }); 
  });
};

ManageAccountDialog.isLoggedIn = function(callback) {
   $.get(
      "command/wikidata/login",
       function(data) {
          callback(data.username);
   });
}; 

ManageAccountDialog.ensureLoggedIn = function(callback) {
    ManageAccountDialog.isLoggedIn(function(logged_in_username) {
        if (logged_in_username == null) {
            ManageAccountDialog.launch(null, callback);
        } else {
            callback(logged_in_username);
        }
    });
};

ManageAccountDialog.checkAndLaunch = function () {
    ManageAccountDialog.isLoggedIn(function(logged_in_username) {
       ManageAccountDialog.launch(logged_in_username, function(success) { });
   });
};
