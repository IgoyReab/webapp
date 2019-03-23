package nl.yogibear.marco;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.awt.*;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
public class MainView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>();
    private TextField filterText = new TextField();

    // add the costomerForm to the main view
    private CustomerForm form = new CustomerForm(this);

    public MainView() {

        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        // adds a button with a click listener

        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            // clears a possible pervious selection from the grid
            grid.asSingleSelect().clear();
            // instantiates a new customer and passes it to customerForm form for editing
            form.setCustomer(new Customer());
        });

        // Positions the button besides the filtertext component.
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerBtn);


        grid.addColumn(Customer::getFirstName).setHeader("First Name");
        grid.addColumn(Customer::getLastName).setHeader("Last Name");
        grid.addColumn(Customer::getStatus).setHeader("Status");

        // add the grid and the costumerForm to the horizontalLayout
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();

        // add the toolbar and the costumerForm to the mainView
        add(toolbar, mainContent);

        setSizeFull();

        updateList();

        // Initially, when no customer is selected in the grid, the form should be hidden
        form.setCustomer(null);

        // Detects when a user selects or deselects a row on the grid.
        // This is a value change listener :
        // addValueChangeListener adds a listener to the Grid. The Grid component supports
        // multi and single-selection modes. This example uses the single-select mode through
        // the asSingleSelect() method.
        // setCustomer sets the selected customer in the CustomerForm. This line also uses
        // the single-select mode.
        // The getValue() method returns the Customer in the selected row or null if there’s
        // no selection, effectively showing or hiding the form accordingly.

        grid.asSingleSelect().addValueChangeListener(event ->
                form.setCustomer(grid.asSingleSelect().getValue()));
    }

    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }
}
