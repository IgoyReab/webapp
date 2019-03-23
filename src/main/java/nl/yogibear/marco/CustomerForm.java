package nl.yogibear.marco;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;


public class CustomerForm extends FormLayout {  // To make this class a UI component its needs to be extended from FormLayout


    // The form needs an input field for each editable property in the Customer class.
    // Vaadin Flow provides different kinds of fields for editing different kinds of values.
    // We use the TextField, ComboBox, and DatePicker components.

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");

    private ComboBox<CustomerStatus> status = new ComboBox<>("Status");
    private DatePicker birthDate = new DatePicker("Birthdate");


    // The form also needs two buttons to save and delete Customer instances.

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");


    // the binder to bind the form to the customer data
    private Binder<Customer> binder = new Binder<>(Customer.class);

    // the save and delete actions need a reference to CustomerService class
    private CustomerService service = CustomerService.getInstance();

    // the save and delete actions also need a reference to the mainview class to
    // ensure that they update the list of customers
    private MainView mainView;


    public CustomerForm(MainView mainView) {
        // the constructor adds the components to the form

        // We need to reference the mainview class to ensure the save
        // and the delete actions update the list of customers in the MainView class

        this.mainView = mainView;

        // adds all the enum values as options to the combobox
        status.setItems(CustomerStatus.values());

        // adds the buttons
        HorizontalLayout buttons = new HorizontalLayout(save, delete);

        // makes the save button prominent
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // adds it all together
        add(firstName, lastName, status, birthDate, buttons);

        // To display specific customer data in the form, you need to connect the properties
        // of a given Customer instance to the input fields in the form.
        // This is known as data binding, and Vaadin Flow provides the Binder helper
        // class for this purpose.

        binder.bindInstanceFields(this);

        // The bindInstanceFields(this) method processes all the instance variables that are
        // input fields (for example, TextField and ComboBox) and maps them (matching by name) to
        // the Java properties in the Customer class. For example, Customer::firstName is mapped
        // to the CustomerForm::firstName input field.

        // implements the save action
        save.addClickListener(event -> save());

        // implements the delete action
        delete.addClickListener(event -> delete());
    }

    // implemntation of the save action
    private void save() {
        // getBean gets the customer instance that was bound to the input fields
        Customer customer = binder.getBean();

        // perform the save action in the backend
        service.save(customer);

        // update the list of customers
        mainView.updateList();

        // hide the form
        setCustomer(null);
    }

    // implementation of the delete action
    private void delete() {
        // getBean gets the customer instance that was bound to the input fields
        Customer customer = binder.getBean();

        // perform the delete action in the backend
        service.delete(customer);

        // update the list of customers
        mainView.updateList();

        // hide the form
        setCustomer(null);
    }


    public void setCustomer(Customer customer) {
        //the logic to show or hide the form in a single public method

        binder.setBean(customer);

//        setBean connects the values in the customer object to the corresponding input fields
//        of the form. When the user changes the value of an input field, the value is set in
//        the corresponding instance variable of the customer object.
//
//        When the customer is:
//          null, the form is hidden.
//
//          is not null, the form is shown, and keyboard focus is placed on the
//          First name input field to allow immediate typing.

        if (customer == null) {
            setVisible(false);
        } else {
            setVisible(true);
            firstName.focus();
        }
    }
}
