package org.kitchensinkfx.view;

import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TitledPaneBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import org.kitchensinkfx.controller.MemberControler;
import org.kitchensinkfx.controller.ServerResponseHandler;
import org.kitchensinkfx.model.Member;
import org.kitchensinkfx.model.Messages;

public class Main extends Application implements EventHandler<ActionEvent>,
		ServerResponseHandler {

	private TextField txtName;
	private TextField txtEmail;
	private TextField txtPhone;

	// Labels with the error messages
	private Label lblMessageName;
	private Label lblMessageEmail;
	private Label lblMessagePhoneNumber;
	private Messages messages;

	private TableView<Member> tableView;

	private Pane mainPane;
	private TitledPane errorPane;
	private ProgressIndicator progressIndicator;

	private final String MAIN_CSS = getClass().getResource(
			"/resources/css/main.css").toString();

	MemberControler controller = new MemberControler();

	public static void main(String[] args) {
          
		launch(args);
	}

	@Override
	public void start(Stage palco) throws Exception {

		List<String> parameters = getParameters().getUnnamed();
		if (parameters.size() > 0) {
			String url = parameters.get(0);
			System.out.println(url);
		}

		StackPane root = new StackPane();
		root.getChildren().addAll(createMainPane(), createProgressIndicator(),
				createErrorPane());
		palco.setScene(SceneBuilder.create().width(500).height(400).root(root)
				.stylesheets(MAIN_CSS).build());
		palco.setTitle("KitchensinkFX");
		palco.setResizable(false);
		palco.show();
		updateData();
	}

	private Pane createMainPane() {
		return mainPane = VBoxBuilder.create()
				.children(createNewMemberForm(), createListMembersPane())
				.build();
	}

	private ProgressIndicator createProgressIndicator() {
		progressIndicator = ProgressIndicatorBuilder.create().maxWidth(100)
				.visible(false).maxHeight(100).build();
		progressIndicator.visibleProperty().bindBidirectional(
				mainPane.disableProperty());
		return progressIndicator;
	}

	private TitledPane createErrorPane() {
		errorPane = TitledPaneBuilder
				.create()
				.text("Error on server communication!")
				.visible(false)
				.prefHeight(150)
				.maxWidth(250)
				.content(
						LabelBuilder
								.create()
								.wrapText(true)
								.maxWidth(200)
								.styleClass("error-label", "panel-error-label ")
								.build()).collapsible(false).build();
		errorPane.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event arg0) {
				errorPane.setVisible(false);
			}
		});
		mainPane.visibleProperty().bind(errorPane.visibleProperty().not());
		return errorPane;
	}

	private Node createNewMemberForm() {
		GridPane newMemberForm = GridPaneBuilder.create().vgap(10).hgap(5)
				.translateX(10).translateY(10).prefHeight(300).build();
		newMemberForm.add(new Label("Name"), 0, 0);
		newMemberForm.add(txtName = new TextField(), 1, 0);
		newMemberForm.add(
				lblMessageName = LabelBuilder.create()
						.styleClass("error-label").maxWidth(250).build(), 2, 0);

		newMemberForm.add(new Label("Email"), 0, 1);
		newMemberForm.add(txtEmail = new TextField(), 1, 1);
		newMemberForm.add(
				lblMessageEmail = LabelBuilder.create()
						.styleClass("error-label").maxWidth(250).build(), 2, 1);

		newMemberForm.add(new Label("Phone"), 0, 2);
		newMemberForm.add(txtPhone = new TextField(), 1, 2);
		newMemberForm.add(lblMessagePhoneNumber = LabelBuilder.create()
				.styleClass("error-label").maxWidth(250).build(), 2, 2);

		newMemberForm.add(
				ButtonBuilder.create().text("Add Member").onAction(this)
						.build(), 1, 3);
		return newMemberForm;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Node createListMembersPane() {
		tableView = new TableView<Member>();

		TableColumn emailColumn = new TableColumn<Member, String>();
		emailColumn.setCellValueFactory(new PropertyValueFactory("email"));
		TableColumn nameColumn = new TableColumn();
		nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
		TableColumn phoneColumn = new TableColumn();
		phoneColumn
				.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

		emailColumn.setText("Email");
		nameColumn.setText("Name");
		phoneColumn.setText("Phone Number");
		phoneColumn.setPrefWidth(150);
		tableView.getColumns().setAll(nameColumn, emailColumn, phoneColumn);
		return tableView;
	}

	private void updateData() {
		progressIndicator.setVisible(true);
		controller.getAllMembers(this);
	}
	private void clearMessagesAndFields() {
		lblMessageEmail.setText("");
		lblMessageName.setText("");
		lblMessagePhoneNumber.setText("");

		txtEmail.setText("");
		txtName.setText("");
		txtPhone.setText("");
	}

	private void showMessages() {
		lblMessageEmail.setText(messages.getEmail());
		lblMessageName.setText(messages.getName());
		lblMessagePhoneNumber.setText(messages.getPhoneNumber());
	}

	private void showErrorMessage(String errorMessage) {
		((Label) errorPane.getContent()).setText(errorMessage);
		errorPane.setVisible(true);
	}

	public void onMembersRetrieve(final List<Member> members) {
		Platform.runLater(new Runnable() {
			public void run() {

				tableView.setItems(FXCollections.observableList(members));
				progressIndicator.setVisible(false);
			}
		});
	}

	public void onMemberCreation(final Messages serverMessages) {
		Platform.runLater(new Runnable() {
			public void run() {
				messages = serverMessages;
				if (serverMessages == null) {
					clearMessagesAndFields();
					updateData();
				} else {
					showMessages();
				}
				progressIndicator.setVisible(false);
			}
		});
	}

	public void onServerError(final Exception e) {
		Platform.runLater(new Runnable() {
			public void run() {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
				progressIndicator.setVisible(false);
			}
		});
	}

	public void handle(ActionEvent evt) {
		progressIndicator.setVisible(true);
		controller.createMember(
				new Member(txtName.getText(), txtEmail.getText(), txtPhone
						.getText()), this);
	}
}