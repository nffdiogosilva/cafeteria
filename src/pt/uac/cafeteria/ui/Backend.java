
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Nuno Silva
 * 
 * This file is part of Cafeteria.
 * 
 * Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cafeteria.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.uac.cafeteria.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import javax.swing.*;
import pt.uac.cafeteria.model.*;
import pt.uac.cafeteria.model.domain.Account.Status;
import pt.uac.cafeteria.model.domain.Address;
import pt.uac.cafeteria.model.domain.Administrator;
import pt.uac.cafeteria.model.domain.Course;
import pt.uac.cafeteria.model.domain.Day;
import pt.uac.cafeteria.model.domain.Meal;
import pt.uac.cafeteria.model.domain.Menu;
import pt.uac.cafeteria.model.domain.Student;
import pt.uac.cafeteria.model.domain.Transaction;
import pt.uac.cafeteria.model.validation.AdministratorValidator;
import pt.uac.cafeteria.model.validation.MenuValidator;
import pt.uac.cafeteria.model.validation.StudentValidator;
import pt.uac.cafeteria.model.validation.Validator;
/**
 * 
 * Represents the Back Office user interface.
 */
public class Backend extends javax.swing.JFrame {

    private static final int LUNCH_HOUR = 13;
    private static final int DINNER_HOUR = 19;

    private static Day today = new Day();

    private Administrator administrator;
    
    private int studentPosition;
    
    /** Type of list to use on JList Component */
    private DefaultListModel studentsList = new DefaultListModel();
    private DefaultListModel adminsList = new DefaultListModel();
    private DefaultListModel validationsList = new DefaultListModel();
    
    /** Creates new form Backend */
    public Backend() {
        
        Application.init();
        
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);
        
        //Panels
        menuPanel.setVisible(false);
        visualizePanel.setVisible(false);
        updatePanel.setVisible(false);
        confirmMealPanel.setVisible(false);
        adminPanel.setVisible(false);
        
        
        //Pop Ups
        loginFrame.setVisible(false);
        warningFrame.setVisible(false);
        warningLogFrame.setVisible(false);
        informationFrame.setVisible(false);
        chargeBalanceFrame.setVisible(false);
    }
    
    private void putFoodInComboBox() {
        
        cbDessert.removeAllItems();
        cbFish.removeAllItems();
        cbMeat.removeAllItems();
        cbVeggie.removeAllItems();
        cbSoup.removeAllItems();
        
        cbDessert.addItem("");
        cbFish.addItem("");
        cbMeat.addItem("");
        cbVeggie.addItem("");
        cbSoup.addItem("");
        
        Collection<String> desserts = MapperRegistry.dessertDish().findAll();
        Collection<String> fishes = MapperRegistry.fishDish().findAll();
        Collection<String> meats = MapperRegistry.meatDish().findAll();
        Collection<String> veggies = MapperRegistry.vegetarianDish().findAll();
        Collection<String> soups = MapperRegistry.soupDish().findAll();
        
        
        for (String dessert : desserts) {
            cbDessert.addItem(dessert);
        }
        
        for (String fish : fishes) {
            cbFish.addItem(fish);
        }
        
        for (String meat : meats) {
            cbMeat.addItem(meat);
        }
        
        for (String veggie : veggies) {
            cbVeggie.addItem(veggie);
        }
        
        for (String soup : soups) {
            cbSoup.addItem(soup);
        }
    }

    private Day getSelectedDay() {

        int selYear = (Integer) cbYearChoice.getSelectedItem();
        int selMonth = cbMonthChoice.getSelectedIndex()+1;

        int selDay = cbDayChoice.getSelectedItem() != null
                  ? (Integer) cbDayChoice.getSelectedItem()
                  : cbDayChoice.getSelectedIndex();

        return new Day(
            selYear,
            selMonth,
            selDay
        );
    }

    private int monthTotalDays (int year, int month) {

        Calendar cal = new GregorianCalendar(year, month, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return days;
    }
        
    private void updateDayItems() {
        cbDayChoice.removeAllItems();
        Integer selectedYear = (Integer) cbYearChoice.getSelectedItem();

        boolean thisMonth = cbMonthChoice.getSelectedIndex() + 1 == today.getMonth() && selectedYear.intValue() == today.getYear();
        int startingDay = thisMonth ? today.getDayOfMonth() : 1;

        for (int i = startingDay; i <= monthTotalDays(selectedYear, cbMonthChoice.getSelectedIndex()); i++) {
            cbDayChoice.addItem(i);
        }

        cbDayChoice.setSelectedIndex(0);

        checkMealTime();
        rbLunch.setSelected(false);
        rbDinner.setSelected(false);

        if (rbLunch.isEnabled() && rbLunch.isSelected() || rbDinner.isEnabled() && rbDinner.isSelected()) {
            chooseMealPanel.setVisible(true);
        }
        else {
            bgTime.clearSelection();
            chooseMealPanel.setVisible(false);
        }
    }

    private void checkMealTime() {
        int currentHour = today.getCalendar().get(Calendar.HOUR_OF_DAY);
        rbLunch.setEnabled(!(getSelectedDay().isToday() && currentHour > LUNCH_HOUR));
        rbDinner.setEnabled(!(getSelectedDay().isToday() && currentHour > DINNER_HOUR));
    }

    private Meal[] makeMeals(Day day, Meal.Time mealTime, String soup,
            String meat, String fish, String vegetarian, String dessert) {

        java.util.List<Meal> meals = new ArrayList<Meal>();

        if (!Validator.isEmpty(meat)) {
            meals.add(new Meal(day, mealTime, Meal.Type.MEAT, soup, meat, dessert));
        }

        if (!Validator.isEmpty(fish)) {
            meals.add(new Meal(day, mealTime, Meal.Type.FISH, soup, fish, dessert));
        }

        if (!Validator.isEmpty(vegetarian)) {
            meals.add(new Meal(day, mealTime, Meal.Type.VEGETARIAN, soup, vegetarian, dessert));
        }

        return meals.toArray(new Meal[]{});
    }

    private int changeStringToInt(String string) {
        try {
            int aux = Integer.parseInt(string);
            return aux;
        }
        catch (NumberFormatException e) {
            e.getMessage();
            return -1;
        }
    }
    
    private int ifIsNull(String number) {
        try {
            if (number.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            e.getMessage();
            return -1;
        }
    }
    
    /** Add a student into JList Component */
    private void putStudentInList(Student student) {
        searchList.setModel(studentsList);
        studentsList.addElement(student);
    }
    
    /** Adds an administrator into JList Component */
    private void putAdminInList(Administrator admin) {
        searchAdminList.setModel(adminsList);
        adminsList.addElement(admin);
    }
    
    private void putValidationInList(String string) {
        validationsJlist.setModel(validationsList);
        validationsList.addElement(string);
    }
    
    /**
     * Method that cleans all JtextField on a component
     * 
     * @param component The component that will be cleaned, i.e: A Panel
     */
    private void clearTextFieldsOf(JComponent component){
        for (int i = 0; i < component.getComponents().length; i++) {
            if (component.getComponent(i) instanceof JTextField) {
                JTextField textField = (JTextField)component.getComponent(i);
                textField.setText(null);
            }
        }
    }
    
    /**
     * Method responsible of setting visible and enabled a component
     * 
     * @param component The component that will be enable
     */
    private void activate(JComponent component) {
        component.setVisible(true);
        component.setEnabled(true);
        for (int i = 0; i < component.getComponents().length; i++) {
            component.getComponent(i).setEnabled(true);
        }
    }
    
    /**
     * Method responsible of setting the component disabled
     * 
     * @param component The component that will be disable
     */
    private void deactivate(JComponent component) {
        component.setEnabled(false);
        for (int i = 0; i < component.getComponents().length; i++) {

            component.getComponent(i).setEnabled(false);
        }
    }
    
    /** 
     * Method responsible of setting the component enabled
     * 
     * @param component The component that will be enabled
     */
    private void enabledAll(JComponent component) {
        component.setEnabled(true);
        for (int i = 0; i < component.getComponents().length; i++) {

            component.getComponent(i).setEnabled(true);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTime = new javax.swing.ButtonGroup();
        bgCharge = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        loginFrame = new javax.swing.JInternalFrame();
        lblLoginMessage = new javax.swing.JLabel();
        btnLoginOk = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        confirm = new javax.swing.JButton();
        lblBackBK = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        validationsFrame = new javax.swing.JInternalFrame();
        btnValidationOk = new javax.swing.JButton();
        lblValidationMessage = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        validationsJlist = new javax.swing.JList();
        warningSearchFrame = new javax.swing.JInternalFrame();
        lblSearchMessage = new javax.swing.JLabel();
        btnSearchOk = new javax.swing.JButton();
        warningLogFrame = new javax.swing.JInternalFrame();
        lblLogMessage = new javax.swing.JLabel();
        btnLogYes = new javax.swing.JButton();
        btnLogNo = new javax.swing.JButton();
        informationFrame = new javax.swing.JInternalFrame();
        lblMessage1 = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        lblMessage2 = new javax.swing.JLabel();
        chargeBalanceFrame = new javax.swing.JInternalFrame();
        lblChargeTitle = new javax.swing.JLabel();
        money = new javax.swing.JTextField();
        btnChargeYes = new javax.swing.JButton();
        btnChargeNo = new javax.swing.JButton();
        informationMealFrame = new javax.swing.JInternalFrame();
        lblMealMessage = new javax.swing.JLabel();
        btnMealOk = new javax.swing.JButton();
        deleteAdminWarningFrame = new javax.swing.JInternalFrame();
        lblDeleteAdminMessage = new javax.swing.JLabel();
        btnDeleteYes1 = new javax.swing.JButton();
        btnDeleteNo1 = new javax.swing.JButton();
        lblDeleteAdminMessage1 = new javax.swing.JLabel();
        deleteWarningFrame = new javax.swing.JInternalFrame();
        lblDeleteMessage = new javax.swing.JLabel();
        btnDeleteYes = new javax.swing.JButton();
        btnDeleteNo = new javax.swing.JButton();
        warningFrame = new javax.swing.JInternalFrame();
        lblMessage = new javax.swing.JLabel();
        btnWarningYes = new javax.swing.JButton();
        btnWarningNo = new javax.swing.JButton();
        confirmMealPanel = new javax.swing.JLayeredPane();
        lblTicketMealDate = new javax.swing.JLabel();
        lblTicketMealTime = new javax.swing.JLabel();
        lblTicketSoup = new javax.swing.JLabel();
        lblTicketMeat = new javax.swing.JLabel();
        lblTicketFish = new javax.swing.JLabel();
        lblTicketVegetarian = new javax.swing.JLabel();
        lblTicketDessert = new javax.swing.JLabel();
        lblTicketMealDateText = new javax.swing.JLabel();
        lbTicketlMealTimeText = new javax.swing.JLabel();
        lblTicketSoupText = new javax.swing.JLabel();
        lblTicketMeatText = new javax.swing.JLabel();
        lblTicketFishText = new javax.swing.JLabel();
        lblTicketVegetarianText = new javax.swing.JLabel();
        lblTicketDessertText = new javax.swing.JLabel();
        btnConfirmMeal = new javax.swing.JButton();
        btnCancelConfirmation = new javax.swing.JButton();
        mealPanel = new javax.swing.JPanel();
        chooseDayPanel = new javax.swing.JPanel();
        cbYearChoice = new javax.swing.JComboBox();
        cbMonthChoice = new javax.swing.JComboBox();
        cbDayChoice = new javax.swing.JComboBox();
        rbLunch = new javax.swing.JRadioButton();
        rbDinner = new javax.swing.JRadioButton();
        chooseMealPanel = new javax.swing.JPanel();
        cbSoup = new javax.swing.JComboBox();
        lblSoup = new javax.swing.JLabel();
        cbMeat = new javax.swing.JComboBox();
        lblMeat = new javax.swing.JLabel();
        cbFish = new javax.swing.JComboBox();
        lblFish = new javax.swing.JLabel();
        cbVeggie = new javax.swing.JComboBox();
        lblVeggie = new javax.swing.JLabel();
        lblDessert = new javax.swing.JLabel();
        cbDessert = new javax.swing.JComboBox();
        btnSaveMeal = new javax.swing.JButton();
        btnCancelMeal = new javax.swing.JButton();
        lblPanelTitle = new javax.swing.JLabel();
        studentPanel = new javax.swing.JTabbedPane();
        addPanel = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblStreet = new javax.swing.JLabel();
        lblPostalCode = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblCourse = new javax.swing.JLabel();
        lblScholarship = new javax.swing.JLabel();
        lblIfen = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        street = new javax.swing.JTextField();
        postalCode = new javax.swing.JTextField();
        postalCode1 = new javax.swing.JTextField();
        city = new javax.swing.JTextField();
        phone = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jbCourse = new javax.swing.JComboBox();
        cbSchollarShip = new javax.swing.JCheckBox();
        lblNumber = new javax.swing.JLabel();
        number = new javax.swing.JTextField();
        searchPanel = new javax.swing.JPanel();
        visualizePanel = new javax.swing.JPanel();
        lblName1 = new javax.swing.JLabel();
        lblStreet1 = new javax.swing.JLabel();
        lblPostalCode1 = new javax.swing.JLabel();
        lblCity1 = new javax.swing.JLabel();
        lblPhone1 = new javax.swing.JLabel();
        lblEmail1 = new javax.swing.JLabel();
        lblCourse1 = new javax.swing.JLabel();
        lblScholarship1 = new javax.swing.JLabel();
        name1 = new javax.swing.JLabel();
        street1 = new javax.swing.JLabel();
        postalCode2 = new javax.swing.JLabel();
        city1 = new javax.swing.JLabel();
        phone1 = new javax.swing.JLabel();
        email1 = new javax.swing.JLabel();
        course = new javax.swing.JLabel();
        cbSchollarship = new javax.swing.JCheckBox();
        lblTitle = new javax.swing.JLabel();
        btnReturn = new javax.swing.JButton();
        btnRecoverCode = new javax.swing.JButton();
        btnUnblockAccount = new javax.swing.JButton();
        btnChargeBalance = new javax.swing.JButton();
        btnCloseAccount = new javax.swing.JButton();
        lblid = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        updatePanel = new javax.swing.JPanel();
        lblName2 = new javax.swing.JLabel();
        lblIfen1 = new javax.swing.JLabel();
        email2 = new javax.swing.JTextField();
        phone2 = new javax.swing.JTextField();
        city2 = new javax.swing.JTextField();
        postalCode3 = new javax.swing.JTextField();
        lblStreet2 = new javax.swing.JLabel();
        btnChange = new javax.swing.JButton();
        lblPostalCode2 = new javax.swing.JLabel();
        name2 = new javax.swing.JTextField();
        street2 = new javax.swing.JTextField();
        lblEmail2 = new javax.swing.JLabel();
        postalCode4 = new javax.swing.JTextField();
        lblCity2 = new javax.swing.JLabel();
        btnCancel2 = new javax.swing.JButton();
        lblPhone2 = new javax.swing.JLabel();
        lblCourse2 = new javax.swing.JLabel();
        lblScholarship2 = new javax.swing.JLabel();
        jbCourse1 = new javax.swing.JComboBox();
        cbSchollarship2 = new javax.swing.JCheckBox();
        lblNumber1 = new javax.swing.JLabel();
        number1 = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        lblSearchNumber = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cbSearch = new javax.swing.JComboBox();
        jScrollPane = new javax.swing.JScrollPane();
        searchList = new javax.swing.JList();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCheck = new javax.swing.JButton();
        adminPanel = new javax.swing.JTabbedPane();
        addAdminPanel = new javax.swing.JPanel();
        lblAdminName = new javax.swing.JLabel();
        lblAdminUser = new javax.swing.JLabel();
        lblAdminPwd = new javax.swing.JLabel();
        adminName = new javax.swing.JTextField();
        adminUser = new javax.swing.JTextField();
        btnCancelAdmin = new javax.swing.JButton();
        btnSaveAdmin = new javax.swing.JButton();
        adminPwd = new javax.swing.JPasswordField();
        searchAdminPanel = new javax.swing.JPanel();
        updateAdminPanel = new javax.swing.JPanel();
        lblAdminName1 = new javax.swing.JLabel();
        btnChange1 = new javax.swing.JButton();
        lblAdminPwd1 = new javax.swing.JLabel();
        setAdminName = new javax.swing.JTextField();
        btnCancel4 = new javax.swing.JButton();
        adminCurrentPwd = new javax.swing.JPasswordField();
        lblAdminPwd2 = new javax.swing.JLabel();
        adminNewPwd = new javax.swing.JPasswordField();
        lblAdminPwd3 = new javax.swing.JLabel();
        adminNewPwd1 = new javax.swing.JPasswordField();
        visualizeAdminPanel = new javax.swing.JPanel();
        lblAdminId = new javax.swing.JLabel();
        lblUserAdmin = new javax.swing.JLabel();
        adminId = new javax.swing.JLabel();
        userAdmin = new javax.swing.JLabel();
        lblTitle1 = new javax.swing.JLabel();
        btnReturn1 = new javax.swing.JButton();
        lblNameAdmin = new javax.swing.JLabel();
        nameAdmin = new javax.swing.JLabel();
        lblSearchAdmin = new javax.swing.JLabel();
        lblSearchNumber1 = new javax.swing.JLabel();
        searchAdmin = new javax.swing.JTextField();
        btnSearchAdmin = new javax.swing.JButton();
        cbSearchAdmin = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchAdminList = new javax.swing.JList();
        btnUpdateAdmin = new javax.swing.JButton();
        btnDeleteAdmin = new javax.swing.JButton();
        btnCheckAdmin = new javax.swing.JButton();
        buttonsPanel = new javax.swing.JPanel();
        btnStudent = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();
        btnMeal = new javax.swing.JButton();
        btnTerminate = new javax.swing.JButton();
        lblBackBK1 = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cafeteria");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        mainPanel.setMinimumSize(new java.awt.Dimension(800, 600));
        mainPanel.setLayout(null);

        loginFrame.setTitle("Aviso");
        loginFrame.setPreferredSize(new java.awt.Dimension(350, 200));

        lblLoginMessage.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblLoginMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginMessage.setText("Username e/ou Password fora dos limites permitidos!");

        btnLoginOk.setText("OK");
        btnLoginOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLoginOkMouseReleased(evt);
            }
        });
        btnLoginOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnLoginOkKeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout loginFrameLayout = new org.jdesktop.layout.GroupLayout(loginFrame.getContentPane());
        loginFrame.getContentPane().setLayout(loginFrameLayout);
        loginFrameLayout.setHorizontalGroup(
            loginFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblLoginMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginFrameLayout.createSequentialGroup()
                .addContainerGap(107, Short.MAX_VALUE)
                .add(btnLoginOk)
                .add(144, 144, 144))
        );
        loginFrameLayout.setVerticalGroup(
            loginFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginFrameLayout.createSequentialGroup()
                .add(14, 14, 14)
                .add(lblLoginMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnLoginOk)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(loginFrame);
        loginFrame.setBounds(225, 170, 350, 150);

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsername.setText("Username:");
        mainPanel.add(lblUsername);
        lblUsername.setBounds(350, 144, 110, 20);

        username.setFont(new java.awt.Font("Tahoma", 1, 11));
        username.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        mainPanel.add(username);
        username.setBounds(350, 170, 110, 30);

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPassword.setText("Password:");
        mainPanel.add(lblPassword);
        lblPassword.setBounds(350, 240, 110, 20);

        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });
        mainPanel.add(password);
        password.setBounds(350, 265, 110, 30);

        confirm.setText("Confirmar");
        confirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                confirmMouseReleased(evt);
            }
        });
        mainPanel.add(confirm);
        confirm.setBounds(360, 340, 90, 30);

        lblBackBK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/backBK.png"))); // NOI18N
        mainPanel.add(lblBackBK);
        lblBackBK.setBounds(0, 0, 800, 600);

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout loginPanelLayout = new org.jdesktop.layout.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 196, Short.MAX_VALUE)
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 396, Short.MAX_VALUE)
        );

        mainPanel.add(loginPanel);
        loginPanel.setBounds(300, 100, 200, 400);

        getContentPane().add(mainPanel, "card3");

        menuPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        menuPanel.setLayout(null);

        validationsFrame.setTitle("Aviso");
        validationsFrame.setPreferredSize(new java.awt.Dimension(400, 300));
        validationsFrame.setSize(new java.awt.Dimension(400, 300));

        btnValidationOk.setText("OK");
        btnValidationOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnValidationOkMouseReleased(evt);
            }
        });

        lblValidationMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblValidationMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValidationMessage.setText("Validações necessárias:");

        validationsJlist.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(validationsJlist);

        org.jdesktop.layout.GroupLayout validationsFrameLayout = new org.jdesktop.layout.GroupLayout(validationsFrame.getContentPane());
        validationsFrame.getContentPane().setLayout(validationsFrameLayout);
        validationsFrameLayout.setHorizontalGroup(
            validationsFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(validationsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .add(validationsFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(validationsFrameLayout.createSequentialGroup()
                        .add(validationsFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                            .add(lblValidationMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, validationsFrameLayout.createSequentialGroup()
                        .add(btnValidationOk)
                        .add(172, 172, 172))))
        );
        validationsFrameLayout.setVerticalGroup(
            validationsFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(validationsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblValidationMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(btnValidationOk)
                .addContainerGap())
        );

        menuPanel.add(validationsFrame);
        validationsFrame.setBounds(300, 100, 400, 300);

        warningSearchFrame.setTitle("Informação");
        warningSearchFrame.setPreferredSize(new java.awt.Dimension(220, 160));

        lblSearchMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblSearchMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchMessage.setText("ID Inválido! Insira Novamente");

        btnSearchOk.setText("OK");
        btnSearchOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSearchOkMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout warningSearchFrameLayout = new org.jdesktop.layout.GroupLayout(warningSearchFrame.getContentPane());
        warningSearchFrame.getContentPane().setLayout(warningSearchFrameLayout);
        warningSearchFrameLayout.setHorizontalGroup(
            warningSearchFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblSearchMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, warningSearchFrameLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .add(btnSearchOk)
                .add(121, 121, 121))
        );
        warningSearchFrameLayout.setVerticalGroup(
            warningSearchFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(warningSearchFrameLayout.createSequentialGroup()
                .add(14, 14, 14)
                .add(lblSearchMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnSearchOk)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuPanel.add(warningSearchFrame);
        warningSearchFrame.setBounds(300, 150, 300, 150);

        warningLogFrame.setTitle("Aviso");
        warningLogFrame.setPreferredSize(new java.awt.Dimension(220, 160));

        lblLogMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblLogMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogMessage.setText("Tem a certeza que deseja Terminar?");

        btnLogYes.setText("Sim");
        btnLogYes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLogYesMouseReleased(evt);
            }
        });

        btnLogNo.setText("Não");
        btnLogNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLogNoMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout warningLogFrameLayout = new org.jdesktop.layout.GroupLayout(warningLogFrame.getContentPane());
        warningLogFrame.getContentPane().setLayout(warningLogFrameLayout);
        warningLogFrameLayout.setHorizontalGroup(
            warningLogFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, warningLogFrameLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnLogYes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnLogNo)
                .add(43, 43, 43))
            .add(lblLogMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        warningLogFrameLayout.setVerticalGroup(
            warningLogFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(warningLogFrameLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(lblLogMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(warningLogFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnLogNo)
                    .add(btnLogYes))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuPanel.add(warningLogFrame);
        warningLogFrame.setBounds(300, 150, 220, 160);

        informationFrame.setTitle("Informação");
        informationFrame.setPreferredSize(new java.awt.Dimension(220, 160));
        informationFrame.getContentPane().setLayout(null);

        lblMessage1.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage1.setText("Dados Guardados Com Sucesso!");
        informationFrame.getContentPane().add(lblMessage1);
        lblMessage1.setBounds(-60, 30, 414, 30);

        btnOk.setText("OK");
        btnOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnOkMouseReleased(evt);
            }
        });
        informationFrame.getContentPane().add(btnOk);
        btnOk.setBounds(110, 80, 75, 29);

        lblMessage2.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        informationFrame.getContentPane().add(lblMessage2);
        lblMessage2.setBounds(50, 50, 220, 20);

        menuPanel.add(informationFrame);
        informationFrame.setBounds(300, 150, 300, 150);

        chargeBalanceFrame.setTitle("Carregamento");
        chargeBalanceFrame.setPreferredSize(new java.awt.Dimension(300, 200));
        chargeBalanceFrame.setSize(new java.awt.Dimension(300, 200));
        chargeBalanceFrame.getContentPane().setLayout(null);

        lblChargeTitle.setFont(new java.awt.Font("Lucida Grande", 0, 14));
        lblChargeTitle.setText("Insira Montante:");
        chargeBalanceFrame.getContentPane().add(lblChargeTitle);
        lblChargeTitle.setBounds(90, 10, 130, 17);
        chargeBalanceFrame.getContentPane().add(money);
        money.setBounds(100, 50, 90, 28);

        btnChargeYes.setText("Confirmar");
        btnChargeYes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnChargeYesMouseReleased(evt);
            }
        });
        chargeBalanceFrame.getContentPane().add(btnChargeYes);
        btnChargeYes.setBounds(40, 110, 90, 30);

        btnChargeNo.setText("Cancelar");
        btnChargeNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnChargeNoMouseReleased(evt);
            }
        });
        chargeBalanceFrame.getContentPane().add(btnChargeNo);
        btnChargeNo.setBounds(150, 110, 90, 30);

        menuPanel.add(chargeBalanceFrame);
        chargeBalanceFrame.setBounds(300, 150, 300, 200);

        informationMealFrame.setTitle("Informação");
        informationMealFrame.setPreferredSize(new java.awt.Dimension(220, 160));
        informationMealFrame.setSize(new java.awt.Dimension(220, 160));

        lblMealMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMealMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMealMessage.setText("Dados guardados com sucesso!");

        btnMealOk.setText("OK");
        btnMealOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMealOkMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout informationMealFrameLayout = new org.jdesktop.layout.GroupLayout(informationMealFrame.getContentPane());
        informationMealFrame.getContentPane().setLayout(informationMealFrameLayout);
        informationMealFrameLayout.setHorizontalGroup(
            informationMealFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(lblMealMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
            .add(informationMealFrameLayout.createSequentialGroup()
                .add(85, 85, 85)
                .add(btnMealOk)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        informationMealFrameLayout.setVerticalGroup(
            informationMealFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(informationMealFrameLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(lblMealMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnMealOk)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        menuPanel.add(informationMealFrame);
        informationMealFrame.setBounds(300, 150, 220, 160);

        deleteAdminWarningFrame.setTitle("Aviso");
        deleteAdminWarningFrame.setMinimumSize(new java.awt.Dimension(335, 165));
        deleteAdminWarningFrame.setPreferredSize(new java.awt.Dimension(335, 165));
        deleteAdminWarningFrame.setSize(new java.awt.Dimension(335, 165));
        deleteAdminWarningFrame.getContentPane().setLayout(null);

        lblDeleteAdminMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblDeleteAdminMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteAdminMessage.setText("este administrador permanentemente?");
        deleteAdminWarningFrame.getContentPane().add(lblDeleteAdminMessage);
        lblDeleteAdminMessage.setBounds(10, 40, 310, 27);

        btnDeleteYes1.setText("Sim");
        btnDeleteYes1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteYes1MouseReleased(evt);
            }
        });
        deleteAdminWarningFrame.getContentPane().add(btnDeleteYes1);
        btnDeleteYes1.setBounds(90, 70, 75, 29);

        btnDeleteNo1.setText("Não");
        btnDeleteNo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteNo1MouseReleased(evt);
            }
        });
        deleteAdminWarningFrame.getContentPane().add(btnDeleteNo1);
        btnDeleteNo1.setBounds(180, 70, 75, 29);

        lblDeleteAdminMessage1.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblDeleteAdminMessage1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteAdminMessage1.setText("Tem a certeza que deseja eliminar");
        deleteAdminWarningFrame.getContentPane().add(lblDeleteAdminMessage1);
        lblDeleteAdminMessage1.setBounds(10, 20, 310, 27);

        menuPanel.add(deleteAdminWarningFrame);
        deleteAdminWarningFrame.setBounds(300, 150, 335, 165);

        deleteWarningFrame.setTitle("Aviso");
        deleteWarningFrame.setPreferredSize(new java.awt.Dimension(390, 160));
        deleteWarningFrame.setSize(new java.awt.Dimension(390, 160));
        deleteWarningFrame.getContentPane().setLayout(null);

        lblDeleteMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblDeleteMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteMessage.setText("Tem a certeza que deseja eliminar este aluno permanentemente?");
        deleteWarningFrame.getContentPane().add(lblDeleteMessage);
        lblDeleteMessage.setBounds(0, 20, 380, 40);

        btnDeleteYes.setText("Sim");
        btnDeleteYes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteYesMouseReleased(evt);
            }
        });
        deleteWarningFrame.getContentPane().add(btnDeleteYes);
        btnDeleteYes.setBounds(110, 70, 75, 29);

        btnDeleteNo.setText("Não");
        btnDeleteNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteNoMouseReleased(evt);
            }
        });
        deleteWarningFrame.getContentPane().add(btnDeleteNo);
        btnDeleteNo.setBounds(200, 70, 75, 29);

        menuPanel.add(deleteWarningFrame);
        deleteWarningFrame.setBounds(300, 150, 390, 160);

        warningFrame.setTitle("Aviso");
        warningFrame.setPreferredSize(new java.awt.Dimension(220, 160));
        warningFrame.setSize(new java.awt.Dimension(220, 160));

        lblMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage.setText("Tem a certeza que deseja Cancelar?");

        btnWarningYes.setText("Sim");
        btnWarningYes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnWarningYesMouseReleased(evt);
            }
        });

        btnWarningNo.setText("Não");
        btnWarningNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnWarningNoMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout warningFrameLayout = new org.jdesktop.layout.GroupLayout(warningFrame.getContentPane());
        warningFrame.getContentPane().setLayout(warningFrameLayout);
        warningFrameLayout.setHorizontalGroup(
            warningFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, warningFrameLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnWarningYes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnWarningNo)
                .add(43, 43, 43))
            .add(lblMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        warningFrameLayout.setVerticalGroup(
            warningFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(warningFrameLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(lblMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(warningFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnWarningNo)
                    .add(btnWarningYes))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuPanel.add(warningFrame);
        warningFrame.setBounds(300, 150, 220, 160);

        confirmMealPanel.setBackground(new java.awt.Color(153, 153, 153));
        confirmMealPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        confirmMealPanel.setMaximumSize(new java.awt.Dimension(500, 600));
        confirmMealPanel.setMinimumSize(new java.awt.Dimension(500, 400));
        confirmMealPanel.setOpaque(true);

        lblTicketMealDate.setText("Data:");
        lblTicketMealDate.setBounds(10, 10, 50, 20);
        confirmMealPanel.add(lblTicketMealDate, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketMealTime.setText("Refeição:");
        lblTicketMealTime.setBounds(10, 40, 70, 20);
        confirmMealPanel.add(lblTicketMealTime, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketSoup.setText("Sopa:");
        lblTicketSoup.setBounds(10, 70, 60, 20);
        confirmMealPanel.add(lblTicketSoup, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketMeat.setText("Carne:");
        lblTicketMeat.setBounds(10, 100, 50, 20);
        confirmMealPanel.add(lblTicketMeat, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketFish.setText("Peixe:");
        lblTicketFish.setBounds(10, 130, 37, 16);
        confirmMealPanel.add(lblTicketFish, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketVegetarian.setText("Vegetariano:");
        lblTicketVegetarian.setBounds(10, 160, 80, 16);
        confirmMealPanel.add(lblTicketVegetarian, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketDessert.setText("Sobremesa:");
        lblTicketDessert.setBounds(10, 190, 80, 20);
        confirmMealPanel.add(lblTicketDessert, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblTicketMealDateText.setText("10/1/2012");
        lblTicketMealDateText.setBounds(100, 10, 90, 20);
        confirmMealPanel.add(lblTicketMealDateText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lbTicketlMealTimeText.setBounds(100, 40, 380, 20);
        confirmMealPanel.add(lbTicketlMealTimeText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblTicketSoupText.setBounds(100, 70, 380, 20);
        confirmMealPanel.add(lblTicketSoupText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblTicketMeatText.setBounds(100, 100, 380, 20);
        confirmMealPanel.add(lblTicketMeatText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblTicketFishText.setBounds(100, 130, 380, 20);
        confirmMealPanel.add(lblTicketFishText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblTicketVegetarianText.setBounds(100, 160, 380, 20);
        confirmMealPanel.add(lblTicketVegetarianText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lblTicketDessertText.setBounds(100, 190, 380, 20);
        confirmMealPanel.add(lblTicketDessertText, javax.swing.JLayeredPane.DEFAULT_LAYER);

        btnConfirmMeal.setText("Confirmar");
        btnConfirmMeal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnConfirmMealMouseReleased(evt);
            }
        });
        btnConfirmMeal.setBounds(140, 340, 107, 29);
        confirmMealPanel.add(btnConfirmMeal, javax.swing.JLayeredPane.DEFAULT_LAYER);

        btnCancelConfirmation.setText("Voltar atrás");
        btnCancelConfirmation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancelConfirmationMouseReleased(evt);
            }
        });
        btnCancelConfirmation.setBounds(260, 340, 116, 29);
        confirmMealPanel.add(btnCancelConfirmation, javax.swing.JLayeredPane.DEFAULT_LAYER);

        menuPanel.add(confirmMealPanel);
        confirmMealPanel.setBounds(220, 50, 500, 400);

        mealPanel.setMaximumSize(new java.awt.Dimension(550, 550));
        mealPanel.setMinimumSize(new java.awt.Dimension(550, 550));
        mealPanel.setPreferredSize(new java.awt.Dimension(550, 550));
        mealPanel.setLayout(null);

        chooseDayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolha a data e a refeição", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));
        chooseDayPanel.setMinimumSize(new java.awt.Dimension(400, 400));
        chooseDayPanel.setPreferredSize(new java.awt.Dimension(333, 155));
        chooseDayPanel.setLayout(null);

        cbYearChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbYearChoiceItemStateChanged(evt);
            }
        });
        chooseDayPanel.add(cbYearChoice);
        cbYearChoice.setBounds(10, 40, 90, 27);

        cbMonthChoice.setMaximumRowCount(12);
        cbMonthChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
        cbMonthChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbMonthChoiceItemStateChanged(evt);
            }
        });
        chooseDayPanel.add(cbMonthChoice);
        cbMonthChoice.setBounds(120, 40, 121, 27);

        cbDayChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cbDayChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbDayChoiceItemStateChanged(evt);
            }
        });
        chooseDayPanel.add(cbDayChoice);
        cbDayChoice.setBounds(240, 40, 72, 27);

        bgTime.add(rbLunch);
        rbLunch.setText("Almoço");
        rbLunch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbLunchMouseReleased(evt);
            }
        });
        chooseDayPanel.add(rbLunch);
        rbLunch.setBounds(10, 97, 80, 23);

        bgTime.add(rbDinner);
        rbDinner.setText("Jantar");
        rbDinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbDinnerMouseReleased(evt);
            }
        });
        chooseDayPanel.add(rbDinner);
        rbDinner.setBounds(10, 123, 68, 23);

        mealPanel.add(chooseDayPanel);
        chooseDayPanel.setBounds(20, 60, 510, 155);

        chooseMealPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chooseMealPanel.add(cbSoup, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 169, -1));

        lblSoup.setText("Sopa:");
        chooseMealPanel.add(lblSoup, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 25, -1, -1));

        chooseMealPanel.add(cbMeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 169, -1));

        lblMeat.setText("Carne:");
        chooseMealPanel.add(lblMeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, -1, -1));

        chooseMealPanel.add(cbFish, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 169, -1));

        lblFish.setText("Peixe:");
        chooseMealPanel.add(lblFish, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 105, -1, -1));

        chooseMealPanel.add(cbVeggie, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 169, -1));

        lblVeggie.setText("Vegetariano:");
        chooseMealPanel.add(lblVeggie, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 145, -1, -1));

        lblDessert.setText("Sobremesa:");
        chooseMealPanel.add(lblDessert, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 185, -1, -1));

        chooseMealPanel.add(cbDessert, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 169, -1));

        btnSaveMeal.setText("Salvar");
        btnSaveMeal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSaveMealMouseReleased(evt);
            }
        });
        chooseMealPanel.add(btnSaveMeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, -1, -1));

        btnCancelMeal.setText("Cancelar");
        btnCancelMeal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancelMealMouseReleased(evt);
            }
        });
        chooseMealPanel.add(btnCancelMeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, -1, -1));

        mealPanel.add(chooseMealPanel);
        chooseMealPanel.setBounds(20, 250, 530, 300);

        lblPanelTitle.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lblPanelTitle.setText("Criar Refeição");
        mealPanel.add(lblPanelTitle);
        lblPanelTitle.setBounds(240, 20, 101, 17);

        menuPanel.add(mealPanel);
        mealPanel.setBounds(220, 10, 550, 550);

        studentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addPanel.setLayout(null);

        lblName.setText("Nome:");
        addPanel.add(lblName);
        lblName.setBounds(20, 26, 41, 16);

        lblStreet.setText("Rua:");
        addPanel.add(lblStreet);
        lblStreet.setBounds(20, 62, 27, 16);

        lblPostalCode.setText("Código Postal:");
        addPanel.add(lblPostalCode);
        lblPostalCode.setBounds(20, 98, 91, 16);

        lblCity.setText("Cidade:");
        addPanel.add(lblCity);
        lblCity.setBounds(20, 134, 47, 16);

        lblPhone.setText("Telefone:");
        addPanel.add(lblPhone);
        lblPhone.setBounds(20, 170, 58, 16);

        lblEmail.setText("Email:");
        addPanel.add(lblEmail);
        lblEmail.setBounds(20, 206, 38, 16);

        lblCourse.setText("Curso:");
        addPanel.add(lblCourse);
        lblCourse.setBounds(20, 242, 41, 16);

        lblScholarship.setText("Bolseiro:");
        addPanel.add(lblScholarship);
        lblScholarship.setBounds(20, 272, 54, 16);

        lblIfen.setText("-");
        addPanel.add(lblIfen);
        lblIfen.setBounds(175, 97, 8, 16);
        addPanel.add(name);
        name.setBounds(121, 20, 406, 28);
        addPanel.add(street);
        street.setBounds(121, 56, 320, 28);
        addPanel.add(postalCode);
        postalCode.setBounds(121, 92, 50, 28);
        addPanel.add(postalCode1);
        postalCode1.setBounds(186, 92, 40, 28);
        addPanel.add(city);
        city.setBounds(121, 128, 406, 28);

        phone.setMaximumSize(new java.awt.Dimension(14, 28));
        addPanel.add(phone);
        phone.setBounds(121, 164, 90, 28);
        addPanel.add(email);
        email.setBounds(121, 200, 406, 28);

        btnCancel.setText("Cancelar");
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancelMouseReleased(evt);
            }
        });
        addPanel.add(btnCancel);
        btnCancel.setBounds(430, 480, 98, 29);

        btnSave.setText("Guardar");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSaveMouseReleased(evt);
            }
        });
        addPanel.add(btnSave);
        btnSave.setBounds(340, 480, 93, 29);

        jbCourse.setPreferredSize(new java.awt.Dimension(250, 25));
        jbCourse.setSize(new java.awt.Dimension(250, 25));
        addPanel.add(jbCourse);
        jbCourse.setBounds(120, 240, 250, 25);
        addPanel.add(cbSchollarShip);
        cbSchollarShip.setBounds(120, 270, 30, 23);

        lblNumber.setText("Nº:");
        addPanel.add(lblNumber);
        lblNumber.setBounds(450, 62, 20, 16);
        addPanel.add(number);
        number.setBounds(477, 56, 50, 28);

        studentPanel.addTab("Adicionar", addPanel);

        searchPanel.setLayout(null);

        visualizePanel.setBackground(new java.awt.Color(153, 153, 153));
        visualizePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        visualizePanel.setLayout(null);

        lblName1.setText("Nome:");
        visualizePanel.add(lblName1);
        lblName1.setBounds(22, 70, 41, 16);

        lblStreet1.setText("Rua:");
        visualizePanel.add(lblStreet1);
        lblStreet1.setBounds(22, 95, 27, 16);

        lblPostalCode1.setText("Código Postal:");
        visualizePanel.add(lblPostalCode1);
        lblPostalCode1.setBounds(22, 120, 91, 16);

        lblCity1.setText("Cidade:");
        visualizePanel.add(lblCity1);
        lblCity1.setBounds(22, 145, 47, 16);

        lblPhone1.setText("Telefone:");
        visualizePanel.add(lblPhone1);
        lblPhone1.setBounds(22, 170, 58, 16);

        lblEmail1.setText("Email:");
        visualizePanel.add(lblEmail1);
        lblEmail1.setBounds(22, 195, 38, 16);

        lblCourse1.setText("Curso:");
        visualizePanel.add(lblCourse1);
        lblCourse1.setBounds(22, 220, 41, 16);

        lblScholarship1.setText("Bolseiro:");
        visualizePanel.add(lblScholarship1);
        lblScholarship1.setBounds(22, 245, 54, 16);

        name1.setText("Nuno Filipe Ferreira Diogo da Silva");
        visualizePanel.add(name1);
        name1.setBounds(143, 70, 217, 16);

        street1.setText("Rua da Universidade, 5");
        visualizePanel.add(street1);
        street1.setBounds(143, 95, 220, 20);

        postalCode2.setText("9500-123");
        visualizePanel.add(postalCode2);
        postalCode2.setBounds(143, 120, 64, 16);

        city1.setText("Ponta Delgada");
        visualizePanel.add(city1);
        city1.setBounds(143, 145, 200, 16);

        phone1.setText("937445045");
        visualizePanel.add(phone1);
        phone1.setBounds(143, 170, 90, 16);

        email1.setText("diogosilva@gmail.com");
        visualizePanel.add(email1);
        email1.setBounds(143, 195, 220, 16);

        course.setText("IRM");
        visualizePanel.add(course);
        course.setBounds(143, 220, 220, 16);

        cbSchollarship.setEnabled(false);
        visualizePanel.add(cbSchollarship);
        cbSchollarship.setBounds(143, 245, 28, 23);

        lblTitle.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        lblTitle.setText("Dados do Aluno");
        visualizePanel.add(lblTitle);
        lblTitle.setBounds(210, 10, 115, 20);

        btnReturn.setText("Voltar");
        btnReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnReturnMouseReleased(evt);
            }
        });
        visualizePanel.add(btnReturn);
        btnReturn.setBounds(240, 290, 81, 29);

        btnRecoverCode.setText("Recuperar Código");
        btnRecoverCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnRecoverCodeMouseReleased(evt);
            }
        });
        visualizePanel.add(btnRecoverCode);
        btnRecoverCode.setBounds(370, 120, 170, 29);

        btnUnblockAccount.setText("Desbloquear Conta");
        btnUnblockAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnUnblockAccountMouseReleased(evt);
            }
        });
        visualizePanel.add(btnUnblockAccount);
        btnUnblockAccount.setBounds(370, 80, 170, 29);

        btnChargeBalance.setText("Carregar Saldo");
        btnChargeBalance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnChargeBalanceMouseReleased(evt);
            }
        });
        visualizePanel.add(btnChargeBalance);
        btnChargeBalance.setBounds(370, 40, 170, 29);

        btnCloseAccount.setText("Fechar Conta");
        btnCloseAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCloseAccountMouseReleased(evt);
            }
        });
        visualizePanel.add(btnCloseAccount);
        btnCloseAccount.setBounds(370, 160, 170, 29);

        lblid.setText("Nº de Aluno:");
        visualizePanel.add(lblid);
        lblid.setBounds(22, 45, 90, 16);

        id.setText("20082439");
        visualizePanel.add(id);
        id.setBounds(143, 45, 64, 16);

        searchPanel.add(visualizePanel);
        visualizePanel.setBounds(10, 100, 550, 330);

        updatePanel.setBackground(new java.awt.Color(153, 153, 153));
        updatePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        updatePanel.setPreferredSize(new java.awt.Dimension(530, 306));
        updatePanel.setLayout(null);

        lblName2.setText("Nome:");
        updatePanel.add(lblName2);
        lblName2.setBounds(20, 26, 41, 16);

        lblIfen1.setText("-");
        updatePanel.add(lblIfen1);
        lblIfen1.setBounds(175, 97, 8, 16);

        email2.setText("nffdiosi@gmail.com");
        updatePanel.add(email2);
        email2.setBounds(121, 200, 380, 28);

        phone2.setText("917235609");
        phone2.setMaximumSize(new java.awt.Dimension(14, 28));
        updatePanel.add(phone2);
        phone2.setBounds(121, 164, 90, 28);

        city2.setText("Ponta Delgada");
        updatePanel.add(city2);
        city2.setBounds(121, 128, 380, 28);

        postalCode3.setText("172");
        updatePanel.add(postalCode3);
        postalCode3.setBounds(186, 92, 40, 28);

        lblStreet2.setText("Rua:");
        updatePanel.add(lblStreet2);
        lblStreet2.setBounds(20, 62, 27, 16);

        btnChange.setText("Alterar");
        btnChange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnChangeMouseReleased(evt);
            }
        });
        updatePanel.add(btnChange);
        btnChange.setBounds(300, 310, 86, 29);

        lblPostalCode2.setText("Código Postal:");
        updatePanel.add(lblPostalCode2);
        lblPostalCode2.setBounds(20, 98, 91, 16);

        name2.setText("Nuno Filipe Ferreira Diogo da Silva");
        updatePanel.add(name2);
        name2.setBounds(121, 20, 380, 28);

        street2.setText("Rua Francisco José, 5");
        updatePanel.add(street2);
        street2.setBounds(121, 56, 300, 28);

        lblEmail2.setText("Email:");
        updatePanel.add(lblEmail2);
        lblEmail2.setBounds(20, 206, 38, 16);

        postalCode4.setText("9500");
        updatePanel.add(postalCode4);
        postalCode4.setBounds(121, 92, 50, 28);

        lblCity2.setText("Cidade:");
        updatePanel.add(lblCity2);
        lblCity2.setBounds(20, 134, 47, 16);

        btnCancel2.setText("Cancelar");
        btnCancel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancel2MouseReleased(evt);
            }
        });
        updatePanel.add(btnCancel2);
        btnCancel2.setBounds(400, 310, 98, 29);

        lblPhone2.setText("Telefone:");
        updatePanel.add(lblPhone2);
        lblPhone2.setBounds(20, 170, 58, 16);

        lblCourse2.setText("Curso:");
        updatePanel.add(lblCourse2);
        lblCourse2.setBounds(20, 242, 41, 16);

        lblScholarship2.setText("Bolseiro:");
        updatePanel.add(lblScholarship2);
        lblScholarship2.setBounds(20, 272, 54, 16);

        jbCourse1.setPreferredSize(new java.awt.Dimension(250, 25));
        jbCourse1.setSize(new java.awt.Dimension(250, 25));
        updatePanel.add(jbCourse1);
        jbCourse1.setBounds(120, 240, 250, 25);
        updatePanel.add(cbSchollarship2);
        cbSchollarship2.setBounds(120, 270, 28, 23);

        lblNumber1.setText("Nº:");
        updatePanel.add(lblNumber1);
        lblNumber1.setBounds(425, 62, 20, 16);
        updatePanel.add(number1);
        number1.setBounds(450, 56, 50, 28);

        searchPanel.add(updatePanel);
        updatePanel.setBounds(20, 100, 530, 350);

        lblSearch.setText("Pesquisar por:");
        searchPanel.add(lblSearch);
        lblSearch.setBounds(150, 30, 100, 30);
        searchPanel.add(lblSearchNumber);
        lblSearchNumber.setBounds(220, 190, 0, 0);

        search.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFocusGained(evt);
            }
        });
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchKeyReleased(evt);
            }
        });
        searchPanel.add(search);
        search.setBounds(240, 70, 100, 28);

        btnSearch.setText("Pesquisar");
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSearchMouseReleased(evt);
            }
        });
        searchPanel.add(btnSearch);
        btnSearch.setBounds(240, 110, 104, 29);

        cbSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nº de aluno", "Nome" }));
        searchPanel.add(cbSearch);
        cbSearch.setBounds(240, 30, 130, 27);

        searchList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                searchListValueChanged(evt);
            }
        });
        jScrollPane.setViewportView(searchList);

        searchPanel.add(jScrollPane);
        jScrollPane.setBounds(20, 202, 530, 132);

        btnUpdate.setText("Actualizar");
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnUpdateMouseReleased(evt);
            }
        });
        searchPanel.add(btnUpdate);
        btnUpdate.setBounds(223, 349, 107, 29);

        btnDelete.setText("Eliminar");
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteMouseReleased(evt);
            }
        });
        searchPanel.add(btnDelete);
        btnDelete.setBounds(397, 349, 95, 29);

        btnCheck.setText("Visualizar");
        btnCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCheckMouseReleased(evt);
            }
        });
        searchPanel.add(btnCheck);
        btnCheck.setBounds(58, 349, 105, 29);

        studentPanel.addTab("Pesquisar", searchPanel);

        menuPanel.add(studentPanel);
        studentPanel.setBounds(220, 10, 570, 580);

        adminPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addAdminPanel.setLayout(null);

        lblAdminName.setText("Nome:");
        addAdminPanel.add(lblAdminName);
        lblAdminName.setBounds(20, 20, 41, 16);

        lblAdminUser.setText("Username:");
        addAdminPanel.add(lblAdminUser);
        lblAdminUser.setBounds(20, 60, 90, 16);

        lblAdminPwd.setText("Password:");
        addAdminPanel.add(lblAdminPwd);
        lblAdminPwd.setBounds(20, 100, 63, 16);
        addAdminPanel.add(adminName);
        adminName.setBounds(121, 15, 406, 28);
        addAdminPanel.add(adminUser);
        adminUser.setBounds(121, 55, 406, 28);

        btnCancelAdmin.setText("Cancelar");
        btnCancelAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancelAdminMouseReleased(evt);
            }
        });
        addAdminPanel.add(btnCancelAdmin);
        btnCancelAdmin.setBounds(290, 370, 98, 29);

        btnSaveAdmin.setText("Guardar");
        btnSaveAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSaveAdminMouseReleased(evt);
            }
        });
        addAdminPanel.add(btnSaveAdmin);
        btnSaveAdmin.setBounds(170, 370, 93, 29);

        adminPwd.setText("jPasswordField1");
        addAdminPanel.add(adminPwd);
        adminPwd.setBounds(120, 95, 134, 28);

        adminPanel.addTab("Adicionar", addAdminPanel);

        searchAdminPanel.setLayout(null);

        updateAdminPanel.setBackground(new java.awt.Color(153, 153, 153));
        updateAdminPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        updateAdminPanel.setPreferredSize(new java.awt.Dimension(530, 306));
        updateAdminPanel.setLayout(null);

        lblAdminName1.setText("Nome:");
        updateAdminPanel.add(lblAdminName1);
        lblAdminName1.setBounds(20, 20, 41, 16);

        btnChange1.setText("Alterar");
        btnChange1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnChange1MouseReleased(evt);
            }
        });
        updateAdminPanel.add(btnChange1);
        btnChange1.setBounds(180, 300, 86, 29);

        lblAdminPwd1.setText("Insira password actual:");
        updateAdminPanel.add(lblAdminPwd1);
        lblAdminPwd1.setBounds(20, 60, 145, 16);

        setAdminName.setText("Nuno Filipe Ferreira Diogo da Silva");
        updateAdminPanel.add(setAdminName);
        setAdminName.setBounds(170, 15, 330, 28);

        btnCancel4.setText("Cancelar");
        btnCancel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancel4MouseReleased(evt);
            }
        });
        updateAdminPanel.add(btnCancel4);
        btnCancel4.setBounds(280, 300, 98, 29);

        adminCurrentPwd.setText("jPasswordField1");
        updateAdminPanel.add(adminCurrentPwd);
        adminCurrentPwd.setBounds(170, 55, 134, 28);

        lblAdminPwd2.setText("Insira nova password:");
        updateAdminPanel.add(lblAdminPwd2);
        lblAdminPwd2.setBounds(20, 100, 137, 16);

        adminNewPwd.setText("jPasswordField1");
        updateAdminPanel.add(adminNewPwd);
        adminNewPwd.setBounds(170, 95, 134, 28);

        lblAdminPwd3.setText("insira novamente:");
        updateAdminPanel.add(lblAdminPwd3);
        lblAdminPwd3.setBounds(20, 140, 112, 16);

        adminNewPwd1.setText("jPasswordField1");
        updateAdminPanel.add(adminNewPwd1);
        adminNewPwd1.setBounds(170, 135, 134, 28);

        searchAdminPanel.add(updateAdminPanel);
        updateAdminPanel.setBounds(20, 100, 530, 350);

        visualizeAdminPanel.setBackground(new java.awt.Color(153, 153, 153));
        visualizeAdminPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        visualizeAdminPanel.setMaximumSize(new java.awt.Dimension(530, 530));

        lblAdminId.setText("ID:");

        lblUserAdmin.setText("Username:");

        adminId.setText("teste");

        userAdmin.setText("teste");

        lblTitle1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        lblTitle1.setText("Dados do Administrador");

        btnReturn1.setText("Voltar");
        btnReturn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnReturn1MouseReleased(evt);
            }
        });

        lblNameAdmin.setText("Nome:");

        nameAdmin.setText("teste");

        org.jdesktop.layout.GroupLayout visualizeAdminPanelLayout = new org.jdesktop.layout.GroupLayout(visualizeAdminPanel);
        visualizeAdminPanel.setLayout(visualizeAdminPanelLayout);
        visualizeAdminPanelLayout.setHorizontalGroup(
            visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(visualizeAdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, visualizeAdminPanelLayout.createSequentialGroup()
                            .add(btnReturn1)
                            .add(221, 221, 221))
                        .add(visualizeAdminPanelLayout.createSequentialGroup()
                            .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblUserAdmin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblAdminId)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblNameAdmin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(adminId)
                                .add(nameAdmin)
                                .add(userAdmin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                            .addContainerGap(184, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, visualizeAdminPanelLayout.createSequentialGroup()
                        .add(lblTitle1)
                        .add(162, 162, 162))))
        );
        visualizeAdminPanelLayout.setVerticalGroup(
            visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(visualizeAdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblTitle1)
                .add(25, 25, 25)
                .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblAdminId)
                    .add(adminId))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNameAdmin)
                    .add(nameAdmin))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(visualizeAdminPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUserAdmin)
                    .add(userAdmin))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 64, Short.MAX_VALUE)
                .add(btnReturn1)
                .addContainerGap())
        );

        searchAdminPanel.add(visualizeAdminPanel);
        visualizeAdminPanel.setBounds(20, 100, 530, 240);

        lblSearchAdmin.setText("Pesquisar por:");
        searchAdminPanel.add(lblSearchAdmin);
        lblSearchAdmin.setBounds(150, 30, 100, 30);
        searchAdminPanel.add(lblSearchNumber1);
        lblSearchNumber1.setBounds(220, 190, 0, 0);

        searchAdmin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchAdminFocusGained(evt);
            }
        });
        searchAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAdminKeyReleased(evt);
            }
        });
        searchAdminPanel.add(searchAdmin);
        searchAdmin.setBounds(240, 70, 100, 28);

        btnSearchAdmin.setText("Pesquisar");
        btnSearchAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSearchAdminMouseReleased(evt);
            }
        });
        searchAdminPanel.add(btnSearchAdmin);
        btnSearchAdmin.setBounds(240, 110, 104, 29);

        cbSearchAdmin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Username", "Nome" }));
        searchAdminPanel.add(cbSearchAdmin);
        cbSearchAdmin.setBounds(240, 30, 120, 27);

        searchAdminList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                searchAdminListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(searchAdminList);

        searchAdminPanel.add(jScrollPane1);
        jScrollPane1.setBounds(20, 202, 530, 132);

        btnUpdateAdmin.setText("Actualizar");
        btnUpdateAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnUpdateAdminMouseReleased(evt);
            }
        });
        searchAdminPanel.add(btnUpdateAdmin);
        btnUpdateAdmin.setBounds(223, 349, 107, 29);

        btnDeleteAdmin.setText("Eliminar");
        btnDeleteAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDeleteAdminMouseReleased(evt);
            }
        });
        searchAdminPanel.add(btnDeleteAdmin);
        btnDeleteAdmin.setBounds(397, 349, 95, 29);

        btnCheckAdmin.setText("Visualizar");
        btnCheckAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCheckAdminMouseReleased(evt);
            }
        });
        searchAdminPanel.add(btnCheckAdmin);
        btnCheckAdmin.setBounds(58, 349, 105, 29);

        adminPanel.addTab("Pesquisar", searchAdminPanel);

        menuPanel.add(adminPanel);
        adminPanel.setBounds(220, 10, 570, 580);

        buttonsPanel.setOpaque(false);

        btnStudent.setText("Estudantes");
        btnStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnStudentMouseReleased(evt);
            }
        });

        btnAdmin.setText("Administradores");
        btnAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnAdminMouseReleased(evt);
            }
        });

        btnMeal.setText("Refeições");
        btnMeal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMealMouseReleased(evt);
            }
        });

        btnTerminate.setText("Terminar");
        btnTerminate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnTerminateMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout buttonsPanelLayout = new org.jdesktop.layout.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(buttonsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnStudent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .add(btnTerminate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .add(btnAdmin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .add(btnMeal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonsPanelLayout.createSequentialGroup()
                .add(29, 29, 29)
                .add(btnStudent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(btnAdmin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(btnMeal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(369, 369, 369)
                .add(btnTerminate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        menuPanel.add(buttonsPanel);
        buttonsPanel.setBounds(0, 0, 200, 600);

        lblBackBK1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/backBK.png"))); // NOI18N
        menuPanel.add(lblBackBK1);
        lblBackBK1.setBounds(0, 0, 800, 600);

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        menuPanel.add(separator);
        separator.setBounds(205, 0, 10, 611);

        getContentPane().add(menuPanel, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStudentMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStudentMouseReleased
        if (btnStudent.isEnabled()) {
            
            //load courses
            jbCourse.addItem(null);
            
            for (Course course: MapperRegistry.course().findAll()) {
                jbCourse.addItem(course);
            }
            btnMeal.setEnabled(true);
            mealPanel.setVisible(false);
            enabledAll(buttonsPanel);
            btnStudent.setEnabled(false);
            activate(studentPanel);
            
            // Add Panel treatment
            activate(addPanel);
            clearTextFieldsOf(addPanel);
            studentPanel.setSelectedIndex(0);
            jbCourse.setSelectedIndex(0);
            
            // Search Panel treatment
            clearTextFieldsOf(searchPanel);
            lblSearch.setEnabled(true);
            cbSearch.setEnabled(true);
            search.setEnabled(true);
            btnSearch.setEnabled(false);
            searchList.setVisible(false);
            studentsList.removeAllElements();
            btnCheck.setVisible(false);
            btnDelete.setVisible(false);
            btnUpdate.setVisible(false);
            searchList.clearSelection();
            updatePanel.setVisible(false);
            
            confirmMealPanel.setVisible(false);
        }
    }//GEN-LAST:event_btnStudentMouseReleased

    private void btnWarningNoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarningNoMouseReleased
        if (btnWarningNo.isEnabled()) {
            warningFrame.setVisible(false);
            if (studentPanel.isVisible()) {
                activate(studentPanel);
                enabledAll(addPanel);
                enabledAll(searchPanel);
                enabledAll(searchList);
                activate(buttonsPanel);
                btnStudent.setEnabled(false);
                
                if (searchList.isSelectionEmpty()) {
                btnCheck.setEnabled(false);
                btnDelete.setEnabled(false);
                btnUpdate.setEnabled(false);
                }
            }
            
            if (adminPanel.isVisible()) {
                activate(adminPanel);
                enabledAll(addAdminPanel);
                enabledAll(searchAdminPanel);
                enabledAll(searchAdminList);
                activate(buttonsPanel);
                btnAdmin.setEnabled(false);
                
                if (searchAdminList.isSelectionEmpty()) {
                btnCheckAdmin.setEnabled(false);
                btnDeleteAdmin.setEnabled(false);
                btnUpdateAdmin.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_btnWarningNoMouseReleased

    private void btnWarningYesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWarningYesMouseReleased
        if (btnWarningYes.isEnabled()) {
            warningFrame.setVisible(false);
            studentPanel.setVisible(false);
            adminPanel.setVisible(false);
            activate(buttonsPanel);
        }
    }//GEN-LAST:event_btnWarningYesMouseReleased

    private void btnOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOkMouseReleased
        lblMessage2.setText(null);
        if (btnOk.isEnabled()) {
            
            informationFrame.setVisible(false);
            
            if (studentPanel.isVisible()) {
                enabledAll(studentPanel);
                enabledAll(addPanel);
                enabledAll(searchPanel);
                enabledAll(searchList);
                enabledAll(buttonsPanel);
                btnStudent.setEnabled(false);
                clearTextFieldsOf(addPanel);
                searchList.clearSelection();
                btnCheck.setEnabled(false);
                btnDelete.setEnabled(false);
                btnUpdate.setEnabled(false);
                btnChargeBalance.setEnabled(true);
                btnUnblockAccount.setEnabled(true);
                btnRecoverCode.setEnabled(true);
                btnCloseAccount.setEnabled(true);
                btnReturn.setEnabled(true);
                cbSchollarShip.setSelected(false);
                jbCourse.setSelectedIndex(0);
            }
            
            if (adminPanel.isVisible()) {
                enabledAll(adminPanel);
                enabledAll(addAdminPanel);
                enabledAll(searchAdminPanel);
                enabledAll(searchAdminList);
                enabledAll(buttonsPanel);
                btnAdmin.setEnabled(false);
                clearTextFieldsOf(addAdminPanel);
                searchAdminList.clearSelection();
                btnCheckAdmin.setEnabled(false);
                btnDeleteAdmin.setEnabled(false);
                btnUpdateAdmin.setEnabled(false);
            }
            
            if (chargeBalanceFrame.isVisible()) {
                enabledAll(chargeBalanceFrame);
            }
            
            if (visualizePanel.isVisible()){
                
                searchList.setSelectedIndex(studentPosition);
                Student student = (Student) searchList.getSelectedValue();
                
                studentPanel.setEnabled(false);
                deactivate(searchPanel);
                deactivate(buttonsPanel);
                searchList.setEnabled(false);
                
                if (student.getAccount().isActive()) {
                    btnUnblockAccount.setEnabled(false);
                }
                
                if (student.getAccount().isClosed()) {
                    visualizePanel.setVisible(false);
                    studentsList.remove(studentPosition);
                    studentPanel.setEnabled(true);
                    enabledAll(searchPanel);
                    enabledAll(buttonsPanel);
                    btnStudent.setEnabled(false);
                }
            }
            
            if (searchList.isSelectionEmpty()) {
                btnUpdate.setEnabled(false);
                btnCheck.setEnabled(false);
                btnDelete.setEnabled(false);
            }
            
            if (mealPanel.isVisible()) {
                enabledAll(buttonsPanel);
                enabledAll(adminPanel);
                enabledAll(chooseMealPanel);
                enabledAll(chooseDayPanel);
                btnMeal.setEnabled(false);
                lblMessage2.setText(null);
            }
        }
    }//GEN-LAST:event_btnOkMouseReleased

    private void btnLogYesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogYesMouseReleased
        if (btnLogYes.isEnabled()) {
            warningLogFrame.setVisible(false);
            warningFrame.setVisible(false);
            informationFrame.setVisible(false);
            menuPanel.setVisible(false);
            mainPanel.setVisible(true);
            activate(buttonsPanel);
        }
    }//GEN-LAST:event_btnLogYesMouseReleased

    private void btnLogNoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogNoMouseReleased
        if (btnLogNo.isEnabled()) {
            btnTerminate.setEnabled(true);
            warningLogFrame.setVisible(false);
            enabledAll(addPanel);
            studentPanel.setEnabled(true);
            activate(buttonsPanel);
            enabledAll(searchPanel);
            enabledAll(searchList);

            enabledAll(addAdminPanel);
            adminPanel.setEnabled(true);
            enabledAll(searchAdminPanel);
            enabledAll(searchAdminList);
            
            enabledAll(chooseMealPanel);
            enabledAll(chooseDayPanel);
            mealPanel.setEnabled(true);
            
            if (searchList.isSelectionEmpty()) {
                btnCheck.setEnabled(false);
                btnDelete.setEnabled(false);
                btnUpdate.setEnabled(false);
            }
            
            if (searchAdminList.isSelectionEmpty()) {
                btnCheckAdmin.setEnabled(false);
                btnDeleteAdmin.setEnabled(false);
                btnUpdateAdmin.setEnabled(false);
            }
            
            if (studentPanel.isVisible()) {
                btnStudent.setEnabled(false);
            }
            
            if (mealPanel.isVisible()) {
                btnMeal.setEnabled(false);
            }
            
            if (confirmMealPanel.isVisible()) {
                btnMeal.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnLogNoMouseReleased

    private void btnTerminateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTerminateMouseReleased
        if (btnTerminate.isEnabled()) {
            btnTerminate.setEnabled(false);
            warningLogFrame.setVisible(true);
            warningFrame.setVisible(false);
            informationFrame.setVisible(false);
            deactivate(studentPanel);
            deactivate(addPanel);
            deactivate(searchPanel);
            deactivate(searchList);
            
            deactivate(adminPanel);
            deactivate(addAdminPanel);
            deactivate(searchAdminPanel);
            deactivate(searchAdminList);
            
            deactivate(mealPanel);
            deactivate(chooseMealPanel);
            deactivate(chooseDayPanel);
            
            deactivate(buttonsPanel);
            clearTextFieldsOf(mainPanel);
        }
    }//GEN-LAST:event_btnTerminateMouseReleased

    private void btnSearchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseReleased
        if (cbSearch.getSelectedIndex() == 0) {
            
            Integer id = ifIsNull(search.getText());
            Student student = MapperRegistry.student().find(id);
            
            if (Validator.testDigits(8, id) && !Validator.isEmpty(search.getText())) {
                
                studentsList.removeAllElements();
                
                if (student != null) {
                    putStudentInList(student);
                }
                else {
                    lblSearchMessage.setText("Não foram encontrados resultados!");

                    warningSearchFrame.setVisible(true);
                    warningSearchFrame.setLocation(300, 180);
                    warningSearchFrame.grabFocus();

                    deactivate(buttonsPanel);
                    deactivate(studentPanel);
                    deactivate(searchPanel);
                    deactivate(searchList);
                }
            }
            else {
            
                lblSearchMessage.setText("Insira numero neste formato: 20121000");

                warningSearchFrame.setVisible(true);
                warningSearchFrame.setLocation(300, 180);
                warningSearchFrame.grabFocus();

                deactivate(buttonsPanel);
                deactivate(studentPanel);
                deactivate(searchPanel);
                deactivate(searchList);

                studentsList.removeAllElements();
            }
        }
        
        else if (cbSearch.getSelectedIndex() == 1 && !Validator.isEmpty(search.getText())) {

            ArrayList<Student> students = (ArrayList<Student>) MapperRegistry.student().findByName(search.getText());

            if (!students.isEmpty()) {

                studentsList.removeAllElements();

                for (Student newStudent : students) {
                    putStudentInList(newStudent);
                }
                
                searchList.setVisible(true);
                searchList.setEnabled(true);
            }
            else {

                lblSearchMessage.setText("Não foram encontrados resultados!");

                warningSearchFrame.setVisible(true);
                warningSearchFrame.setLocation(300, 180);
                warningSearchFrame.grabFocus();

                deactivate(buttonsPanel);
                deactivate(studentPanel);
                deactivate(searchPanel);
                deactivate(searchList);

                studentsList.removeAllElements();
            }
        }
        else {
            lblSearchMessage.setText("Insira um nome neste formato: Ana");

            warningSearchFrame.setVisible(true);
            warningSearchFrame.setLocation(300, 180);
            warningSearchFrame.grabFocus();

            deactivate(buttonsPanel);
            deactivate(studentPanel);
            deactivate(searchPanel);
            deactivate(searchList);
            
            studentsList.removeAllElements();
        }
        
        if (btnSearch.isEnabled()) {

            searchList.setVisible(true);
            searchList.setEnabled(true);

            btnCheck.setVisible(true);
            btnCheck.setEnabled(false);

            btnUpdate.setVisible(true);
            btnUpdate.setEnabled(false);

            btnDelete.setVisible(true);
            btnDelete.setEnabled(false);
        }
    }//GEN-LAST:event_btnSearchMouseReleased

    private void passwordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyReleased
        
        String user = username.getText();
        String pass = String.valueOf(password.getPassword());
            
        administrator = Application.authenticateAdmin(user, pass);
        
        if (confirm.isEnabled() && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            if (administrator != null) {
                mainPanel.setVisible(false);
                menuPanel.setVisible(true);
                studentPanel.setVisible(false);
                adminPanel.setVisible(false);
                mealPanel.setVisible(false);
                activate(buttonsPanel);
            }
            else {
                lblLoginMessage.setText("Dados Inválidos! Tente Novamente");
                loginFrame.setVisible(true);
                username.setEnabled(false);
                password.setEnabled(false);
                confirm.setEnabled(false);
            }
        }
    }//GEN-LAST:event_passwordKeyReleased

    private void confirmMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmMouseReleased
        
        String user = username.getText();
        String pass = String.valueOf(password.getPassword());
            
        administrator = Application.authenticateAdmin(user, pass);
        
        if (administrator != null && confirm.isEnabled()) {
            mainPanel.setVisible(false);
            menuPanel.setVisible(true);
            studentPanel.setVisible(false);
            adminPanel.setVisible(false);
            mealPanel.setVisible(false);
            confirmMealPanel.setVisible(false);
            activate(buttonsPanel);
        }
        else {
            lblLoginMessage.setText("Dados Inválidos! Tente Novamente");
            loginFrame.setVisible(true);
            username.setEnabled(false);
            password.setEnabled(false);
            confirm.setEnabled(false);
        }
    }//GEN-LAST:event_confirmMouseReleased

    private void btnCheckMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckMouseReleased
        if (btnCheck.isEnabled()) {
            
            Student student = (Student) searchList.getSelectedValue();
            
            id.setText(student.getId().toString());
            name1.setText(student.getName());
            street1.setText(student.getAddress().getStreetAddress() + ", " + student.getAddress().getNumber());
            postalCode2.setText(student.getAddress().getPostalCode());
            city1.setText(student.getAddress().getCity());
            phone1.setText(student.getPhone().toString());
            email1.setText(student.getEmail());
            course.setText(student.getCourse().toString());
            
            if (student.hasScholarship()) {
                cbSchollarship.setSelected(true);
            }
            else {
                cbSchollarship.setSelected(false);
            }
            
            if (student.getAccount().isActive()) {
                btnUnblockAccount.setEnabled(false);
            }
            
            if (student.getAccount().isBlocked()) {
                btnUnblockAccount.setEnabled(true);
            }
            
            visualizePanel.setVisible(true);
            deactivate(studentPanel);
            deactivate(searchPanel);
            deactivate(searchList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnCheckMouseReleased

    private void searchListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_searchListValueChanged

        btnCheck.setEnabled(true);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_searchListValueChanged

    private void btnReturnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnMouseReleased
        visualizePanel.setVisible(false);
        activate(studentPanel);
        activate(searchPanel);
        activate(searchList);
        activate(buttonsPanel);
        btnStudent.setEnabled(false);
    }//GEN-LAST:event_btnReturnMouseReleased

    private void searchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFocusGained
        btnSearch.setEnabled(true);
    }//GEN-LAST:event_searchFocusGained

    private void searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cbSearch.getSelectedIndex() == 0) {
                
                Integer id = ifIsNull(search.getText());
                Student student = MapperRegistry.student().find(id);

                if (Validator.testDigits(8, id) && !Validator.isEmpty(search.getText())) {
                    
                    studentsList.removeAllElements();

                    if (student != null) {
                        putStudentInList(student);
                    }
                    else {
                        lblSearchMessage.setText("Não foram encontrados resultados!");

                        warningSearchFrame.setVisible(true);
                        warningSearchFrame.setLocation(300, 180);
                        warningSearchFrame.grabFocus();

                        deactivate(buttonsPanel);
                        deactivate(studentPanel);
                        deactivate(searchPanel);
                        deactivate(searchList);
                    }
                }
                else {

                    lblSearchMessage.setText("Insira numero neste formato: 20121000");

                    warningSearchFrame.setVisible(true);
                    warningSearchFrame.setLocation(300, 180);
                    warningSearchFrame.grabFocus();

                    deactivate(buttonsPanel);
                    deactivate(studentPanel);
                    deactivate(searchPanel);
                    deactivate(searchList);

                    studentsList.removeAllElements();
                }
            }

            else if (cbSearch.getSelectedIndex() == 1 && !Validator.isEmpty(search.getText())) {

                ArrayList<Student> students = (ArrayList<Student>) MapperRegistry.student().findByName(search.getText());

                if (!students.isEmpty()) {

                    studentsList.removeAllElements();

                    for (Student student : students) {
                        putStudentInList(student);
                    }

                    searchList.setVisible(true);
                    searchList.setEnabled(true);
                }
                else {

                    lblSearchMessage.setText("Não foram encontrados resultados!");

                    warningSearchFrame.setVisible(true);
                    warningSearchFrame.setLocation(300, 180);
                    warningSearchFrame.grabFocus();

                    deactivate(buttonsPanel);
                    deactivate(studentPanel);
                    deactivate(searchPanel);
                    deactivate(searchList);

                    studentsList.removeAllElements();
                }
            }
            else {
                lblSearchMessage.setText("Insira um nome neste formato: Ana");

                warningSearchFrame.setVisible(true);
                warningSearchFrame.setLocation(300, 180);
                warningSearchFrame.grabFocus();

                deactivate(buttonsPanel);
                deactivate(studentPanel);
                deactivate(searchPanel);
                deactivate(searchList);

                studentsList.removeAllElements();
            }

            if (btnSearch.isEnabled()) {

                searchList.setVisible(true);
                searchList.setEnabled(true);

                btnCheck.setVisible(true);
                btnCheck.setEnabled(false);

                btnUpdate.setVisible(true);
                btnUpdate.setEnabled(false);

                btnDelete.setVisible(true);
                btnDelete.setEnabled(false);
            }
        }
    }//GEN-LAST:event_searchKeyReleased

    private void btnSaveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseReleased
        
        StudentValidator validator = new StudentValidator();

        Course course = (Course)jbCourse.getSelectedItem();

        Address address = new Address(street.getText(), 
                number.getText(), 
                postalCode.getText() + "-" + postalCode1.getText(), 
                city.getText());

        Student student = new Student(
                null, 
                name.getText(), 
                address, 
                ifIsNull(phone.getText()), 
                email.getText(), 
                cbSchollarShip.isSelected(), 
                course
                );

        if (validator.isValid(student) && btnSave.isEnabled()) {
            
            Integer studentId = null;
            Student studentByEmail = MapperRegistry.student().findByEmail(student.getEmail());
            
            if (studentByEmail != null) {
                    lblMessage1.setText("Email: " + student.getEmail() + " já registado!");
                    informationFrame.setVisible(true);             
                    deactivate(studentPanel);             
                    deactivate(addPanel);
                    deactivate(buttonsPanel);
            }
            
            try {
                studentId = Application.createStudent(student);
            }
            catch (ApplicationException e) {
                ApplicationException.log(e);
            }

            if (studentId != null ) {
                
                String subject = "Dados da conta";
                String body = "Olá, " + student.getName()
                        + "\n\nJá se encontram disponíveis os seus dados de acesso ao Sistema Cafeteria:\n"
                        + "\nNº de Conta: " + studentId
                        + "\nCódigo de Acesso: " + student.getAccount().getPinCode()
                        + "\nSaldo Actual: " + student.getAccount().getBalance()
                        + "\n\nCom os melhores cumprimentos,\nA Administração.";
                try {
                    Application.sendMail(student.getEmail(), subject, body);
                    
                    lblMessage1.setText("Dados enviados para " + student.getEmail());
                    informationFrame.setVisible(true);             
                    deactivate(studentPanel);             
                    deactivate(addPanel);
                    deactivate(buttonsPanel);
                } catch (ApplicationException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    //@TODO: PERGUNTAR AO HELDER AQUI
                    lblMessage1.setText("Dados Guardados!");
            
                    informationFrame.setVisible(true);
                    deactivate(studentPanel);
                    deactivate(addPanel);
                    deactivate(buttonsPanel);
                }
            }
            
        }
        else {
            deactivate(studentPanel);             
            deactivate(addPanel);
            deactivate(buttonsPanel);
            
            validationsFrame.setVisible(true);
            
            validationsList.removeAllElements();
            for (String validation: validator.getErrors()) {
                putValidationInList(validation);
            }
        }
    }//GEN-LAST:event_btnSaveMouseReleased

    private void btnCancelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseReleased
        if (btnCancel.isEnabled()) {       
            warningFrame.setVisible(true);    
            deactivate(studentPanel);   
            deactivate(addPanel);
            deactivate(buttonsPanel);
        }     
    }//GEN-LAST:event_btnCancelMouseReleased

    private void btnDeleteYesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteYesMouseReleased
        deleteWarningFrame.setVisible(false);
        
        Student student = (Student) searchList.getSelectedValue();
        
        if (MapperRegistry.student().delete(student) && MapperRegistry.account().delete(student.getAccount()) && MapperRegistry.address().delete(student.getAddress())) {
            
            studentsList.remove(searchList.getSelectedIndex());
            
            lblMessage1.setText("Eliminação feita com Sucesso");
            
            informationFrame.setVisible(true);
            deactivate(searchPanel);
            deactivate(searchList);
        }
        else {
            lblMessage1.setText("Não foi possível eliminar o estudante!");
            
            informationFrame.setVisible(true);
            deactivate(searchPanel);
            deactivate(searchList);
        }
        
        
    }//GEN-LAST:event_btnDeleteYesMouseReleased

    private void btnDeleteNoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteNoMouseReleased
        deleteWarningFrame.setVisible(false);
        enabledAll(studentPanel);   
        enabledAll(searchPanel);
        enabledAll(searchList);
        enabledAll(buttonsPanel);
        btnStudent.setEnabled(false);
    }//GEN-LAST:event_btnDeleteNoMouseReleased

    private void btnDeleteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseReleased
        if (btnDelete.isEnabled()) {
            deleteWarningFrame.setVisible(true);
            deactivate(studentPanel);   
            deactivate(searchPanel);
            deactivate(searchList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnDeleteMouseReleased

    private void btnChangeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeMouseReleased
        if (btnChange.isEnabled()) {
            
            Student student = (Student) searchList.getSelectedValue();
            
            String auxName = student.getName();
            String auxStreet =  student.getAddress().getStreetAddress();
            String auxNumber = student.getAddress().getNumber();
            String auxPostalCode = student.getAddress().getPostalCode();
            String auxCity = student.getAddress().getCity();
            String auxEmail = student.getEmail();
            Integer auxPhone = student.getPhone();
            boolean auxSchollarship = student.hasScholarship();
            Course auxCourse = student.getCourse();
            
            Course course = (Course) jbCourse1.getSelectedItem();
            
            int id = changeStringToInt(this.id.getText());
            
            String postalCode = postalCode4.getText() + "-" + postalCode3.getText();
            
            Address address = student.getAddress();
            
            address.setStreetAddress(street2.getText());
            address.setNumber(number1.getText());
            address.setPostalCode(postalCode);
            address.setCity(city2.getText());
            
            student.setId(id);
            student.setName(name2.getText());
            student.setAddress(address);
            student.setPhone(ifIsNull(phone2.getText()));
            student.setEmail(email2.getText());
            student.setScholarship(cbSchollarship2.isSelected());
            student.setCourse(course);
            
            StudentValidator validator = new StudentValidator();
            
            if (validator.isValid(student)) {
                if (MapperRegistry.student().update(student)) {
                    informationFrame.setVisible(true);
                    updatePanel.setVisible(false);
                }
            }
            else {
                
                student.setName(auxName);
                student.getAddress().setStreetAddress(auxStreet);
                student.getAddress().setNumber(auxNumber);
                student.getAddress().setPostalCode(auxPostalCode);
                student.getAddress().setCity(auxCity);
                student.setPhone(auxPhone);
                student.setEmail(auxEmail);
                student.setScholarship(auxSchollarship);
                student.setCourse(auxCourse);
                
                deactivate(updatePanel);

                validationsFrame.setVisible(true);

                validationsList.removeAllElements();
                for (String validation: validator.getErrors()) {
                    putValidationInList(validation);
                }
            }
        }
    }//GEN-LAST:event_btnChangeMouseReleased

    private void btnCancel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancel2MouseReleased
        if (btnCancel2.isEnabled()) {
            updatePanel.setVisible(false);
            activate(studentPanel);
            activate(searchPanel);
            activate(searchList);
            enabledAll(buttonsPanel);
            btnStudent.setEnabled(false);
        }
    }//GEN-LAST:event_btnCancel2MouseReleased

    private void btnUpdateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseReleased
        if (btnUpdate.isEnabled()) {
            
            for (Course course: MapperRegistry.course().findAll()) {
                jbCourse1.addItem(course);
            }
            
            Student student = (Student) searchList.getSelectedValue();
            
            String aux[] = student.getAddress().getPostalCode().split("-");
            
            id.setText(student.getId().toString());
            name2.setText(student.getName());
            street2.setText(student.getAddress().getStreetAddress());
            number1.setText(student.getAddress().getNumber());
            postalCode4.setText(aux[0]);
            postalCode3.setText(aux[1]);
            city2.setText(student.getAddress().getCity());
            phone2.setText(student.getPhone().toString());
            email2.setText(student.getEmail());
            jbCourse1.setSelectedIndex(student.getCourse().getId() - 1);
            
            if (student.hasScholarship()) {
                cbSchollarship2.setSelected(true);
            }
            else {
                cbSchollarship2.setSelected(false);
            }
            
            lblMessage1.setText("Dados guardados com sucesso!");
            updatePanel.setVisible(true);
            deactivate(studentPanel);   
            deactivate(searchPanel);
            deactivate(searchList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnUpdateMouseReleased

    private void btnMealMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMealMouseReleased

        for (int i = 0; i < 2; i++) {
            cbYearChoice.addItem(today.getYear() + i);
        }

        if (btnMeal.isEnabled()) {
            bgTime.clearSelection();
            activate(buttonsPanel);
            studentPanel.setVisible(false);
            adminPanel.setVisible(false);
            
            btnMeal.setEnabled(false);
            
            mealPanel.setVisible(true);
            chooseMealPanel.setEnabled(true);
            chooseMealPanel.setVisible(false);
            confirmMealPanel.setVisible(false);
            
            cbYearChoice.setEnabled(true);
            cbYearChoice.setSelectedIndex(0);
            
            cbMonthChoice.setEnabled(true);
            cbMonthChoice.setSelectedIndex(0);
            
            updateDayItems();
            
            cbDayChoice.setEnabled(true);
            cbDayChoice.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnMealMouseReleased

    private void rbLunchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbLunchMouseReleased
        putFoodInComboBox();
        
        if (rbLunch.isEnabled() && rbLunch.isSelected() || rbDinner.isEnabled() && rbDinner.isSelected()) {
            chooseMealPanel.setVisible(true);
            enabledAll(chooseMealPanel);
        }
        else {
            chooseMealPanel.setVisible(false);
        }
    }//GEN-LAST:event_rbLunchMouseReleased

    private void btnCancelMealMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMealMouseReleased
        if (btnCancelMeal.isEnabled()) {
            btnCancelMeal.setEnabled(false);
            mealPanel.setVisible(false);
            menuPanel.setVisible(true);
            activate(buttonsPanel);
        }
        
    }//GEN-LAST:event_btnCancelMealMouseReleased

    private void btnSaveMealMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMealMouseReleased

        String meat = "" + cbMeat.getSelectedItem();
        String fish = "" + cbFish.getSelectedItem();
        String veggie = "" + cbVeggie.getSelectedItem();
        String soup = "" + cbSoup.getSelectedItem();
        String dessert = "" + cbDessert.getSelectedItem();
        
        if (meat.isEmpty() && fish.isEmpty() && veggie.isEmpty() || dessert.isEmpty() || soup.isEmpty()) {
            lblMessage1.setText("Preencher pelo menos um prato Principal!");
            lblMessage2.setText("Acompanhado de Sopa e Sobremessa.");
            informationFrame.setVisible(true);
            deactivate(buttonsPanel);
            deactivate(mealPanel);
            deactivate(chooseMealPanel);
            deactivate(chooseDayPanel);
            
        }
        else {
            if (btnSaveMeal.isEnabled()) {
                mealPanel.setVisible(false);
                activate(confirmMealPanel);
                deactivate(buttonsPanel);

                lblTicketMealDateText.setText(cbDayChoice.getSelectedItem() + "/" + (cbMonthChoice.getSelectedIndex() + 1) + "/" + cbYearChoice.getSelectedItem());

                if (rbLunch.isSelected()) {
                    lbTicketlMealTimeText.setText(rbLunch.getText());
                }
                else {
                    lbTicketlMealTimeText.setText(rbDinner.getText());
                }

                lblTicketSoupText.setText("" + cbSoup.getSelectedItem());
                lblTicketMeatText.setText("" + cbMeat.getSelectedItem());
                lblTicketFishText.setText("" + cbFish.getSelectedItem());
                lblTicketVegetarianText.setText("" + cbVeggie.getSelectedItem());
                lblTicketDessertText.setText("" + cbDessert.getSelectedItem());
            }
        }
                
    }//GEN-LAST:event_btnSaveMealMouseReleased

    private void btnCancelConfirmationMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelConfirmationMouseReleased
        enabledAll(buttonsPanel);
        btnMeal.setEnabled(false);
        confirmMealPanel.setVisible(false);
        mealPanel.setVisible(true);
    }//GEN-LAST:event_btnCancelConfirmationMouseReleased

    private void btnConfirmMealMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmMealMouseReleased

        Day day = getSelectedDay();

        Menu menu = MapperRegistry.menu().find(day);
        if (menu == null) {
            menu = new Menu(day);
        }
        Meal.Time mealTime = rbLunch.isSelected() ? Meal.Time.LUNCH : Meal.Time.DINNER;

        String soup = lblTicketSoupText.getText();
        String meat = lblTicketMeatText.getText();
        String fish = lblTicketFishText.getText();
        String vegetarian = lblTicketVegetarianText.getText();
        String dessert = lblTicketDessertText.getText();

        menu.addMeals(makeMeals(day, mealTime, soup, meat, fish, vegetarian, dessert));
        
        MenuValidator validator = new MenuValidator();

        if (btnConfirmMeal.isEnabled() && validator.isValid(menu) && MapperRegistry.menu().insert(menu) != null) {
            lblMessage1.setText("Dados guardados com sucesso!");
            informationMealFrame.setVisible(true);
            deactivate(mealPanel);
            deactivate(confirmMealPanel);
            deactivate(buttonsPanel);
        }
        else {
            lblMessage1.setText("Não foi possível guardar dados!");
            informationMealFrame.setVisible(true);
            deactivate(mealPanel);
            deactivate(confirmMealPanel);
            deactivate(buttonsPanel);
        }
        
    }//GEN-LAST:event_btnConfirmMealMouseReleased

    private void btnMealOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMealOkMouseReleased
        confirmMealPanel.setVisible(false);
        informationMealFrame.setVisible(false);
        activate(mealPanel);
        enabledAll(buttonsPanel);
        
        chooseDayPanel.setVisible(true);
        chooseMealPanel.setVisible(false);
        btnMeal.setEnabled(false);
        clearTextFieldsOf(chooseMealPanel);
        bgTime.clearSelection();
        searchAdminList.clearSelection();
    }//GEN-LAST:event_btnMealOkMouseReleased

    private void rbDinnerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbDinnerMouseReleased
        putFoodInComboBox();
        
        if (rbLunch.isEnabled() && rbLunch.isSelected() || rbDinner.isEnabled() && rbDinner.isSelected()) {
            chooseMealPanel.setVisible(true);
            enabledAll(chooseMealPanel);
        }
        else {
            chooseMealPanel.setVisible(false);
        }
    }//GEN-LAST:event_rbDinnerMouseReleased

        private void btnAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAdminMouseReleased
            if (btnAdmin.isEnabled()) {
                activate(buttonsPanel);
                enabledAll(adminPanel);
                enabledAll(addAdminPanel);
                enabledAll(searchAdminPanel);
                btnAdmin.setEnabled(false);
                adminPanel.setVisible(true);
                adminPanel.setSelectedIndex(0);
                clearTextFieldsOf(addAdminPanel);
                clearTextFieldsOf(searchAdminPanel);
                
                studentPanel.setVisible(false);
                mealPanel.setVisible(false);
                updateAdminPanel.setVisible(false);
                visualizeAdminPanel.setVisible(false);
                
                btnSearchAdmin.setEnabled(false);
                searchAdminList.setVisible(false);
                searchAdminList.setEnabled(false);
                searchAdminList.clearSelection();
                searchAdmin.setText(null);
                
                btnUpdateAdmin.setEnabled(false);
                btnCheckAdmin.setEnabled(false);
                btnDeleteAdmin.setEnabled(false);
                
                confirmMealPanel.setVisible(false);
            }
    }//GEN-LAST:event_btnAdminMouseReleased

    private void btnSearchAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchAdminMouseReleased
        
        if (!Validator.isEmpty(searchAdmin.getText())) {
        
            if (cbSearchAdmin.getSelectedIndex() == 0) {
                
                Administrator admin = MapperRegistry.administrator().findByUsername(searchAdmin.getText());

                if (admin != null) {

                    adminsList.removeAllElements();

                    putAdminInList(admin);

                    searchAdminList.setVisible(true);
                    searchAdminList.setEnabled(true);
                    
                    if (searchAdminList.isSelectionEmpty()) {
                        btnCheckAdmin.setEnabled(false);
                        btnUpdateAdmin.setEnabled(false);
                        btnDeleteAdmin.setEnabled(false);
                    }
                }
                else {

                    lblSearchMessage.setText("Não foram encontrados resultados!");

                    warningSearchFrame.setVisible(true);
                    warningSearchFrame.setLocation(300, 180);
                    warningSearchFrame.grabFocus();

                    deactivate(buttonsPanel);
                    deactivate(adminPanel);
                    deactivate(searchAdminPanel);
                    deactivate(searchAdminList);

                    adminsList.removeAllElements();
                }
            }
            else {
                ArrayList<Administrator> admins = (ArrayList<Administrator>) MapperRegistry.administrator().findByName(searchAdmin.getText());
                
                if (!admins.isEmpty()) {
                    adminsList.removeAllElements();
                    
                    for (Administrator newAdmin : admins) {
                        putAdminInList(newAdmin);
                    }
                    
                    searchAdminList.setVisible(true);
                    searchAdminList.setEnabled(true);
                    
                    if (searchAdminList.isSelectionEmpty()) {
                        btnCheckAdmin.setEnabled(false);
                        btnUpdateAdmin.setEnabled(false);
                        btnDeleteAdmin.setEnabled(false);
                    }
                }
                else {

                    lblSearchMessage.setText("Não foram encontrados resultados!");

                    warningSearchFrame.setVisible(true);
                    warningSearchFrame.setLocation(300, 180);
                    warningSearchFrame.grabFocus();

                    deactivate(buttonsPanel);
                    deactivate(adminPanel);
                    deactivate(searchAdminPanel);
                    deactivate(searchAdminList);

                    adminsList.removeAllElements();
                }
            }
        }
        else {
            lblSearchMessage.setText("Insira um nome neste formato: Ana");

            warningSearchFrame.setVisible(true);
            warningSearchFrame.setLocation(300, 180);
            warningSearchFrame.grabFocus();

            deactivate(buttonsPanel);
            deactivate(adminPanel);
            deactivate(searchAdminPanel);
            deactivate(searchAdminList);
            
            adminsList.removeAllElements();
        }
        
        if (btnSearch.isEnabled()) {

            searchAdminList.setVisible(true);
            searchAdminList.setEnabled(true);

            btnCheckAdmin.setVisible(true);
            btnCheckAdmin.setEnabled(false);

            btnUpdateAdmin.setVisible(true);
            btnUpdateAdmin.setEnabled(false);

            btnDeleteAdmin.setVisible(true);
            btnDeleteAdmin.setEnabled(false);
        }
    }//GEN-LAST:event_btnSearchAdminMouseReleased

    private void searchAdminFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchAdminFocusGained
        btnSearchAdmin.setEnabled(true);
    }//GEN-LAST:event_searchAdminFocusGained

    private void searchAdminListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_searchAdminListValueChanged
        btnCheckAdmin.setEnabled(true);
        btnUpdateAdmin.setEnabled(true);
        btnDeleteAdmin.setEnabled(true);
    }//GEN-LAST:event_searchAdminListValueChanged

    private void btnCheckAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckAdminMouseReleased
        Administrator admin = (Administrator) searchAdminList.getSelectedValue();
        
        adminId.setText(String.valueOf(admin.getId()));
        nameAdmin.setText(admin.getName());
        userAdmin.setText(admin.getUsername());
        
        if (btnCheckAdmin.isEnabled()) {
            visualizeAdminPanel.setVisible(true);
            deactivate(adminPanel);
            deactivate(searchAdminPanel);
            deactivate(searchAdminList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnCheckAdminMouseReleased

    private void btnReturn1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturn1MouseReleased
        visualizeAdminPanel.setVisible(false);
        activate(adminPanel);
        activate(searchAdminPanel);
        activate(searchAdminList);
        activate(buttonsPanel);
        btnAdmin.setEnabled(false);
    }//GEN-LAST:event_btnReturn1MouseReleased

    private void btnUpdateAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateAdminMouseReleased
        Administrator admin = (Administrator) searchAdminList.getSelectedValue();
        
        setAdminName.setText(admin.getName());
        adminCurrentPwd.setText(null);
        adminNewPwd.setText(null);
        adminNewPwd1.setText(null);
        
        if (btnUpdateAdmin.isEnabled()) {
            updateAdminPanel.setVisible(true);
            deactivate(adminPanel);   
            deactivate(searchAdminPanel);
            deactivate(searchAdminList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnUpdateAdminMouseReleased

    private void btnCancel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancel4MouseReleased
        updateAdminPanel.setVisible(false);
        activate(adminPanel);
        activate(searchAdminPanel);
        activate(searchAdminList);
        activate(buttonsPanel);
        btnAdmin.setEnabled(false);
    }//GEN-LAST:event_btnCancel4MouseReleased

    private void btnDeleteAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAdminMouseReleased
        if (btnDeleteAdmin.isEnabled()) {
            deleteAdminWarningFrame.setVisible(true);
            deactivate(adminPanel);   
            deactivate(searchAdminPanel);
            deactivate(searchAdminList);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnDeleteAdminMouseReleased

    private void btnDeleteYes1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteYes1MouseReleased
        
        Administrator admin = (Administrator) searchAdminList.getSelectedValue();
        
        if (MapperRegistry.administrator().delete(admin)) {
            adminsList.remove(searchAdminList.getSelectedIndex());
            lblMessage1.setText("Eliminação feita com Sucesso");
        }
        else {
            lblMessage1.setText("Impossível efectuar operação!");
        }
        
        deleteAdminWarningFrame.setVisible(false);
        informationFrame.setVisible(true);
        deactivate(searchAdminPanel);
        deactivate(searchAdminList);
    }//GEN-LAST:event_btnDeleteYes1MouseReleased

    private void btnDeleteNo1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteNo1MouseReleased
        deleteAdminWarningFrame.setVisible(false);
        enabledAll(adminPanel);   
        enabledAll(searchAdminPanel);
        enabledAll(searchAdminList);
        enabledAll(buttonsPanel);
        btnAdmin.setEnabled(false);
    }//GEN-LAST:event_btnDeleteNo1MouseReleased

    private void btnChange1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChange1MouseReleased
        if (btnChange1.isEnabled()) {
            Administrator admin = (Administrator) searchAdminList.getSelectedValue();
        
            String name = setAdminName.getText();
            String pass = String.valueOf(adminCurrentPwd.getPassword());
            String newPass = String.valueOf(adminNewPwd.getPassword());
            String newPass1 = String.valueOf(adminNewPwd1.getPassword());
            String aux = admin.getName();

            AdministratorValidator validator = new AdministratorValidator();

            if (pass.isEmpty() && newPass.isEmpty() && newPass1.isEmpty()) {
                
                admin.setName(name);
                
                if (validator.isValid(admin)) {
                    
                    if (MapperRegistry.administrator().update(admin) && btnChange1.isEnabled()) {
                        lblMessage1.setText("Dados guardados com sucesso!");
                    }
                    else {
                        lblMessage1.setText("Não foi possível guardar dados");
                    }
                    informationFrame.setVisible(true);
                    updateAdminPanel.setVisible(false);
                }
                else {
                    
                    admin.setName(aux);
                    deactivate(updateAdminPanel);
                    
                    validationsFrame.setVisible(true);
                    validationsFrame.requestFocusInWindow();
                    validationsFrame.grabFocus();

                    validationsList.removeAllElements();
                    for (String validation: validator.getErrors()) {
                        putValidationInList(validation);
                    }
                }
            }
            else {

                if (admin.getPassword().equals(pass) && newPass.equals(newPass1)) {

                    admin.setPassword(newPass);
                    admin.setName(name);

                    if (validator.isValid(admin)) {

                        if (MapperRegistry.administrator().update(admin)) {
                            lblMessage1.setText("Dados guardados com sucesso!");
                        }
                        else {
                            lblMessage1.setText("Não foi possível guardar dados");
                        }
                        informationFrame.setVisible(true);
                        updateAdminPanel.setVisible(false);
                    }
                    else {
                        deactivate(updateAdminPanel);

                        validationsFrame.setVisible(true);
                        validationsFrame.requestFocusInWindow();
                        validationsFrame.grabFocus();

                        validationsList.removeAllElements();
                        for (String validation: validator.getErrors()) {
                            putValidationInList(validation);
                        }
                    }
                }
                else if (!admin.getPassword().equals(pass)){
                    lblMessage1.setText("Password incorrecta!");

                    informationFrame.setVisible(true);
                    updateAdminPanel.setVisible(false);
                }
                else if (!newPass.equals(newPass1)) {
                    lblMessage1.setText("Nova password incompatível com a re-inserida!");

                    informationFrame.setVisible(true);
                    updateAdminPanel.setVisible(false);
                }
                else {
                    System.out.println(validator.getErrors());
                }   
            }
        }
    }//GEN-LAST:event_btnChange1MouseReleased

    private void btnCancelAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelAdminMouseReleased
        if (btnCancelAdmin.isEnabled()) {       
            warningFrame.setVisible(true);    
            deactivate(adminPanel);   
            deactivate(addAdminPanel);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnCancelAdminMouseReleased

    private void btnSaveAdminMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveAdminMouseReleased
        AdministratorValidator validator = new AdministratorValidator();
        
        Administrator admin = new Administrator (adminName.getText(), adminUser.getText(), String.valueOf(adminPwd.getPassword()));

        if (validator.isValid(admin) && btnSaveAdmin.isEnabled()) {
            
            Administrator adminByUser = MapperRegistry.administrator().findByUsername(adminUser.getText());
            
            if (adminByUser != null) {
                    lblMessage1.setText("Username: " + adminUser.getText() + " já registado!");
                    
            }
            else {
                
                if (MapperRegistry.administrator().insert(admin) != null) {
                    lblMessage1.setText("Dados guardados com sucesso!");
                }
                else {
                    lblMessage1.setText("Não foi possível guardar dados!");
                }
            }
            informationFrame.setVisible(true);             
            deactivate(adminPanel);             
            deactivate(addAdminPanel);
            deactivate(buttonsPanel);
        }
        else {
            
            deactivate(adminPanel);             
            deactivate(addAdminPanel);
            deactivate(buttonsPanel);
            
            validationsFrame.setVisible(true);
            
            validationsList.removeAllElements();
            for (String validation: validator.getErrors()) {
                putValidationInList(validation);
            }
        }
    }//GEN-LAST:event_btnSaveAdminMouseReleased

    private void btnChargeBalanceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChargeBalanceMouseReleased
        money.requestFocus();
        if (btnChargeBalance.isEnabled()) {
            btnChargeBalance.setEnabled(false);
            btnUnblockAccount.setEnabled(false);
            btnRecoverCode.setEnabled(false);
            btnCloseAccount.setEnabled(false);
            btnReturn.setEnabled(false);
            btnStudent.setEnabled(false);
            
            chargeBalanceFrame.setVisible(true);
            bgCharge.clearSelection();
            money.setText(null);
        }
    }//GEN-LAST:event_btnChargeBalanceMouseReleased

    private void btnChargeNoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChargeNoMouseReleased
        Student student = (Student) searchList.getSelectedValue();
        
        if (student.getAccount().isActive()) {
            btnUnblockAccount.setEnabled(false);
        }
        else {
            btnUnblockAccount.setEnabled(true);
        }
        btnChargeBalance.setEnabled(true);
        btnRecoverCode.setEnabled(true);
        btnCloseAccount.setEnabled(true);
        btnReturn.setEnabled(true);
        
        chargeBalanceFrame.setVisible(false);
    }//GEN-LAST:event_btnChargeNoMouseReleased

    private void btnChargeYesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChargeYesMouseReleased
        Student student = (Student) searchList.getSelectedValue();
        studentPosition = searchList.getSelectedIndex();
        
        if (Validator.matchPattern("^[0-9]+$", money.getText())) {
            
            Double value = Double.parseDouble(money.getText());
            
            student.getAccount().deposit(value, administrator.getUsername());
            
            chargeBalanceFrame.setVisible(false);
            
            if(MapperRegistry.account().update(student.getAccount())) {
                
                int last = student.getAccount().getTransactions().size() - 1;
                
                Transaction lastTransaction = student.getAccount().getTransactions().get(last);
                
                String subject = "Carregamento efectuado";
                String body = "Olá, " + student.getName()
                        + "\n\nFoi efectuado um carregamento na sua conta:\n"
                        + "\nNº de Conta: " + student.getId() + "\n"
                        + "\nData: " + lastTransaction.getDate() + "\n"
                        + "\nValor Carregado: " + lastTransaction.getAmount() + "\n"
                        + "\nSaldo Actual: " + student.getAccount().getBalance()
                        + "\n\nCom os melhores cumprimentos,\nA Administração.";
                try {
                    Application.sendMail(student.getEmail(), subject, body);
                    
                    lblMessage1.setText("Dados da operação enviados para: ");
                    lblMessage2.setText(student.getEmail());
                    informationFrame.setVisible(true);
                    searchList.setEnabled(false);
                } catch (ApplicationException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
                
            }
            else {
                lblMessage1.setText("Operação falhou!");
                informationFrame.setVisible(true);
                searchList.setEnabled(false);
            }
        }
        else {
            
            lblMessage1.setText("Insira um número por favor");
            informationFrame.setVisible(true);
            money.setText(null);
            
            deactivate(studentPanel);             
            deactivate(searchPanel);
            deactivate(buttonsPanel);
            chargeBalanceFrame.setVisible(false);
        }
    }//GEN-LAST:event_btnChargeYesMouseReleased

    private void btnUnblockAccountMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnblockAccountMouseReleased
        
        searchList.setSelectedIndex(studentPosition);
        Student student = (Student) searchList.getSelectedValue();
        
        if (btnUnblockAccount.isEnabled() && student.getAccount().isBlocked()) {
            btnChargeBalance.setEnabled(false);
            btnUnblockAccount.setEnabled(false);
            btnCloseAccount.setEnabled(false);
            btnRecoverCode.setEnabled(false);
            btnReturn.setEnabled(false);
            
            deactivate(studentPanel);
            deactivate(searchList);
            deactivate(searchPanel);
            deactivate(buttonsPanel);
            
            student.getAccount().setStatus(Status.ACTIVE);
            
            if (MapperRegistry.account().update(student.getAccount())) {
                lblMessage1.setText("Conta desbloqueada");
            }
            else {
                lblMessage1.setText("Não foi possível efectuar operação!");
            }
            informationFrame.setVisible(true);
        }
    }//GEN-LAST:event_btnUnblockAccountMouseReleased

    private void btnRecoverCodeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRecoverCodeMouseReleased
        
        searchList.setSelectedIndex(studentPosition);
        Student student = (Student) searchList.getSelectedValue();
        
        String subject = "Recuperação de Código de acesso";
        String body = "Olá, " + student.getName()
                + "\n\n Código de Acesso: " + student.getAccount().getPinCode()
                + "\n\nCom os melhores cumprimentos,\nA Administração.";
        try {

            if (btnRecoverCode.isEnabled()) {
                btnChargeBalance.setEnabled(false);
                btnUnblockAccount.setEnabled(false);
                btnRecoverCode.setEnabled(false);
                btnCloseAccount.setEnabled(false);
                btnReturn.setEnabled(false);

                deactivate(studentPanel);
                deactivate(searchList);
                deactivate(searchPanel);
                deactivate(buttonsPanel);

                Application.sendMail(student.getEmail(), subject, body);
                
                lblMessage1.setText("Código enviado para: " + student.getEmail());
                informationFrame.setVisible(true);
            }

        } catch (ApplicationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

            lblMessage1.setText("Não foi possível enviar email!");

            informationFrame.setVisible(true);             
            deactivate(studentPanel);             
            deactivate(addPanel);
            deactivate(buttonsPanel);
        }
    }//GEN-LAST:event_btnRecoverCodeMouseReleased

    private void btnCloseAccountMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseAccountMouseReleased
        
        searchList.setSelectedIndex(studentPosition);
        Student student = (Student) searchList.getSelectedValue();
        
        if (btnCloseAccount.isEnabled() && student != null) {
            
            Application.closeStudentAccount(student);
            
            btnChargeBalance.setEnabled(false);
            btnUnblockAccount.setEnabled(false);
            btnRecoverCode.setEnabled(false);
            btnCloseAccount.setEnabled(false);
            btnReturn.setEnabled(false);
            
            deactivate(studentPanel);
            deactivate(searchList);
            deactivate(searchPanel);
            deactivate(buttonsPanel);
            
            lblMessage1.setText("Conta fechada!");
            
            informationFrame.setVisible(true);
        }
    }//GEN-LAST:event_btnCloseAccountMouseReleased

    private void btnLoginOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginOkMouseReleased
        loginFrame.setVisible(false);
        username.setText(null);
        password.setText(null);
        
        username.setEnabled(true);
        password.setEnabled(true);
        confirm.setEnabled(true);
        
        username.grabFocus();
    }//GEN-LAST:event_btnLoginOkMouseReleased

    private void btnLoginOkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginOkKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loginFrame.setVisible(false);
            username.setText(null);
            password.setText(null);

            username.setEnabled(true);
            password.setEnabled(true);
            confirm.setEnabled(true);

            username.grabFocus();
        }
    }//GEN-LAST:event_btnLoginOkKeyReleased

    private void btnSearchOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchOkMouseReleased

        warningSearchFrame.setVisible(false);
        
        if (studentPanel.isVisible()) {
            enabledAll(buttonsPanel);
            enabledAll(studentPanel);
            enabledAll(searchList);
            enabledAll(searchPanel);

            btnStudent.setEnabled(false);
            
            if (studentsList.isEmpty()) {
                btnCheck.setEnabled(false);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        }
        
        if (adminPanel.isVisible()) {
            enabledAll(buttonsPanel);
            enabledAll(adminPanel);
            enabledAll(searchAdminList);
            enabledAll(searchAdminPanel);

            btnAdmin.setEnabled(false);
            
            if (adminsList.isEmpty()) {
                btnCheckAdmin.setEnabled(false);
                btnUpdateAdmin.setEnabled(false);
                btnDeleteAdmin.setEnabled(false);
            }
        }
        
        
        
    }//GEN-LAST:event_btnSearchOkMouseReleased

    private void searchAdminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchAdminKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!Validator.isEmpty(searchAdmin.getText())) {
        
                if (cbSearchAdmin.getSelectedIndex() == 0) {

                    Administrator admin = MapperRegistry.administrator().findByUsername(searchAdmin.getText());

                    if (admin != null) {

                        adminsList.removeAllElements();

                        putAdminInList(admin);

                        searchAdminList.setVisible(true);
                        searchAdminList.setEnabled(true);

                        if (searchAdminList.isSelectionEmpty()) {
                            btnCheckAdmin.setEnabled(false);
                            btnUpdateAdmin.setEnabled(false);
                            btnDeleteAdmin.setEnabled(false);
                        }
                    }
                    else {

                        lblSearchMessage.setText("Não foram encontrados resultados!");

                        warningSearchFrame.setVisible(true);
                        warningSearchFrame.setLocation(300, 180);
                        warningSearchFrame.grabFocus();

                        deactivate(buttonsPanel);
                        deactivate(adminPanel);
                        deactivate(searchAdminPanel);
                        deactivate(searchAdminList);

                        adminsList.removeAllElements();
                    }
                }
                else {
                    ArrayList<Administrator> admins = (ArrayList<Administrator>) MapperRegistry.administrator().findByName(searchAdmin.getText());

                    if (!admins.isEmpty()) {
                        adminsList.removeAllElements();

                        for (Administrator newAdmin : admins) {
                            putAdminInList(newAdmin);
                        }

                        searchAdminList.setVisible(true);
                        searchAdminList.setEnabled(true);

                        if (searchAdminList.isSelectionEmpty()) {
                            btnCheckAdmin.setEnabled(false);
                            btnUpdateAdmin.setEnabled(false);
                            btnDeleteAdmin.setEnabled(false);
                        }
                    }
                    else {

                        lblSearchMessage.setText("Não foram encontrados resultados!");

                        warningSearchFrame.setVisible(true);
                        warningSearchFrame.setLocation(300, 180);
                        warningSearchFrame.grabFocus();

                        deactivate(buttonsPanel);
                        deactivate(adminPanel);
                        deactivate(searchAdminPanel);
                        deactivate(searchAdminList);

                        adminsList.removeAllElements();
                    }
                }
            }
            else {
                lblSearchMessage.setText("Insira um nome neste formato: Ana");

                warningSearchFrame.setVisible(true);
                warningSearchFrame.setLocation(300, 180);
                warningSearchFrame.grabFocus();

                deactivate(buttonsPanel);
                deactivate(adminPanel);
                deactivate(searchAdminPanel);
                deactivate(searchAdminList);

                adminsList.removeAllElements();
            }

            if (btnSearch.isEnabled()) {

                searchAdminList.setVisible(true);
                searchAdminList.setEnabled(true);

                btnCheckAdmin.setVisible(true);
                btnCheckAdmin.setEnabled(false);

                btnUpdateAdmin.setVisible(true);
                btnUpdateAdmin.setEnabled(false);

                btnDeleteAdmin.setVisible(true);
                btnDeleteAdmin.setEnabled(false);
            }
        }
        
    }//GEN-LAST:event_searchAdminKeyReleased

    private void btnValidationOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnValidationOkMouseReleased
        validationsFrame.setVisible(false);
        if (addPanel.isVisible()) {
            enabledAll(studentPanel);             
            enabledAll(addPanel);
            enabledAll(buttonsPanel);
            btnStudent.setEnabled(false);
        }
        
        if (updateAdminPanel.isVisible()) {
            enabledAll(updateAdminPanel);
            btnStudent.setEnabled(true);
            btnAdmin.setEnabled(false);
        }
        
        if (updatePanel.isVisible()) {
            enabledAll(updatePanel);
            enabledAll(buttonsPanel);
            btnStudent.setEnabled(false);
            searchList.setSelectedIndex(studentPosition);
        }
        
        if (addAdminPanel.isVisible()) {
            enabledAll(adminPanel);             
            enabledAll(addAdminPanel);
            enabledAll(buttonsPanel);
            btnAdmin.setEnabled(false);
        }
    }//GEN-LAST:event_btnValidationOkMouseReleased

    private void cbMonthChoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMonthChoiceItemStateChanged
        updateDayItems();
    }//GEN-LAST:event_cbMonthChoiceItemStateChanged

    private void cbYearChoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbYearChoiceItemStateChanged
        updateDayItems();
    }//GEN-LAST:event_cbYearChoiceItemStateChanged

    private void cbDayChoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDayChoiceItemStateChanged

        checkMealTime();

        if (rbLunch.isEnabled() && rbLunch.isSelected() || rbDinner.isEnabled() && rbDinner.isSelected()) {
            chooseMealPanel.setVisible(true);
        }
        else {
            bgTime.clearSelection();
            chooseMealPanel.setVisible(false);
        }
    }//GEN-LAST:event_cbDayChoiceItemStateChanged
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Backend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Backend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Backend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Backend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            Application.init();
            MapperRegistry.account().setAutosave(true);
            
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Backend().setVisible(true);
                }
            }); 
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            
            ApplicationException.log(e);
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addAdminPanel;
    private javax.swing.JPanel addPanel;
    private javax.swing.JPasswordField adminCurrentPwd;
    private javax.swing.JLabel adminId;
    private javax.swing.JTextField adminName;
    private javax.swing.JPasswordField adminNewPwd;
    private javax.swing.JPasswordField adminNewPwd1;
    private javax.swing.JTabbedPane adminPanel;
    private javax.swing.JPasswordField adminPwd;
    private javax.swing.JTextField adminUser;
    private javax.swing.ButtonGroup bgCharge;
    private javax.swing.ButtonGroup bgTime;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancel2;
    private javax.swing.JButton btnCancel4;
    private javax.swing.JButton btnCancelAdmin;
    private javax.swing.JButton btnCancelConfirmation;
    private javax.swing.JButton btnCancelMeal;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnChange1;
    private javax.swing.JButton btnChargeBalance;
    private javax.swing.JButton btnChargeNo;
    private javax.swing.JButton btnChargeYes;
    private javax.swing.JButton btnCheck;
    private javax.swing.JButton btnCheckAdmin;
    private javax.swing.JButton btnCloseAccount;
    private javax.swing.JButton btnConfirmMeal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAdmin;
    private javax.swing.JButton btnDeleteNo;
    private javax.swing.JButton btnDeleteNo1;
    private javax.swing.JButton btnDeleteYes;
    private javax.swing.JButton btnDeleteYes1;
    private javax.swing.JButton btnLogNo;
    private javax.swing.JButton btnLogYes;
    private javax.swing.JButton btnLoginOk;
    private javax.swing.JButton btnMeal;
    private javax.swing.JButton btnMealOk;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRecoverCode;
    private javax.swing.JButton btnReturn;
    private javax.swing.JButton btnReturn1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAdmin;
    private javax.swing.JButton btnSaveMeal;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchAdmin;
    private javax.swing.JButton btnSearchOk;
    private javax.swing.JButton btnStudent;
    private javax.swing.JButton btnTerminate;
    private javax.swing.JButton btnUnblockAccount;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpdateAdmin;
    private javax.swing.JButton btnValidationOk;
    private javax.swing.JButton btnWarningNo;
    private javax.swing.JButton btnWarningYes;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox cbDayChoice;
    private javax.swing.JComboBox cbDessert;
    private javax.swing.JComboBox cbFish;
    private javax.swing.JComboBox cbMeat;
    private javax.swing.JComboBox cbMonthChoice;
    private javax.swing.JCheckBox cbSchollarShip;
    private javax.swing.JCheckBox cbSchollarship;
    private javax.swing.JCheckBox cbSchollarship2;
    private javax.swing.JComboBox cbSearch;
    private javax.swing.JComboBox cbSearchAdmin;
    private javax.swing.JComboBox cbSoup;
    private javax.swing.JComboBox cbVeggie;
    private javax.swing.JComboBox cbYearChoice;
    private javax.swing.JInternalFrame chargeBalanceFrame;
    private javax.swing.JPanel chooseDayPanel;
    private javax.swing.JPanel chooseMealPanel;
    private javax.swing.JTextField city;
    private javax.swing.JLabel city1;
    private javax.swing.JTextField city2;
    private javax.swing.JButton confirm;
    private javax.swing.JLayeredPane confirmMealPanel;
    private javax.swing.JLabel course;
    private javax.swing.JInternalFrame deleteAdminWarningFrame;
    private javax.swing.JInternalFrame deleteWarningFrame;
    private javax.swing.JTextField email;
    private javax.swing.JLabel email1;
    private javax.swing.JTextField email2;
    private javax.swing.JLabel id;
    private javax.swing.JInternalFrame informationFrame;
    private javax.swing.JInternalFrame informationMealFrame;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox jbCourse;
    private javax.swing.JComboBox jbCourse1;
    private javax.swing.JLabel lbTicketlMealTimeText;
    private javax.swing.JLabel lblAdminId;
    private javax.swing.JLabel lblAdminName;
    private javax.swing.JLabel lblAdminName1;
    private javax.swing.JLabel lblAdminPwd;
    private javax.swing.JLabel lblAdminPwd1;
    private javax.swing.JLabel lblAdminPwd2;
    private javax.swing.JLabel lblAdminPwd3;
    private javax.swing.JLabel lblAdminUser;
    private javax.swing.JLabel lblBackBK;
    private javax.swing.JLabel lblBackBK1;
    private javax.swing.JLabel lblChargeTitle;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCity1;
    private javax.swing.JLabel lblCity2;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblCourse1;
    private javax.swing.JLabel lblCourse2;
    private javax.swing.JLabel lblDeleteAdminMessage;
    private javax.swing.JLabel lblDeleteAdminMessage1;
    private javax.swing.JLabel lblDeleteMessage;
    private javax.swing.JLabel lblDessert;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmail1;
    private javax.swing.JLabel lblEmail2;
    private javax.swing.JLabel lblFish;
    private javax.swing.JLabel lblIfen;
    private javax.swing.JLabel lblIfen1;
    private javax.swing.JLabel lblLogMessage;
    private javax.swing.JLabel lblLoginMessage;
    private javax.swing.JLabel lblMealMessage;
    private javax.swing.JLabel lblMeat;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblMessage1;
    private javax.swing.JLabel lblMessage2;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblName2;
    private javax.swing.JLabel lblNameAdmin;
    private javax.swing.JLabel lblNumber;
    private javax.swing.JLabel lblNumber1;
    private javax.swing.JLabel lblPanelTitle;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPhone1;
    private javax.swing.JLabel lblPhone2;
    private javax.swing.JLabel lblPostalCode;
    private javax.swing.JLabel lblPostalCode1;
    private javax.swing.JLabel lblPostalCode2;
    private javax.swing.JLabel lblScholarship;
    private javax.swing.JLabel lblScholarship1;
    private javax.swing.JLabel lblScholarship2;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearchAdmin;
    private javax.swing.JLabel lblSearchMessage;
    private javax.swing.JLabel lblSearchNumber;
    private javax.swing.JLabel lblSearchNumber1;
    private javax.swing.JLabel lblSoup;
    private javax.swing.JLabel lblStreet;
    private javax.swing.JLabel lblStreet1;
    private javax.swing.JLabel lblStreet2;
    private javax.swing.JLabel lblTicketDessert;
    private javax.swing.JLabel lblTicketDessertText;
    private javax.swing.JLabel lblTicketFish;
    private javax.swing.JLabel lblTicketFishText;
    private javax.swing.JLabel lblTicketMealDate;
    private javax.swing.JLabel lblTicketMealDateText;
    private javax.swing.JLabel lblTicketMealTime;
    private javax.swing.JLabel lblTicketMeat;
    private javax.swing.JLabel lblTicketMeatText;
    private javax.swing.JLabel lblTicketSoup;
    private javax.swing.JLabel lblTicketSoupText;
    private javax.swing.JLabel lblTicketVegetarian;
    private javax.swing.JLabel lblTicketVegetarianText;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblUserAdmin;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblValidationMessage;
    private javax.swing.JLabel lblVeggie;
    private javax.swing.JLabel lblid;
    private javax.swing.JInternalFrame loginFrame;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mealPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTextField money;
    private javax.swing.JTextField name;
    private javax.swing.JLabel name1;
    private javax.swing.JTextField name2;
    private javax.swing.JLabel nameAdmin;
    private javax.swing.JTextField number;
    private javax.swing.JTextField number1;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField phone;
    private javax.swing.JLabel phone1;
    private javax.swing.JTextField phone2;
    private javax.swing.JTextField postalCode;
    private javax.swing.JTextField postalCode1;
    private javax.swing.JLabel postalCode2;
    private javax.swing.JTextField postalCode3;
    private javax.swing.JTextField postalCode4;
    private javax.swing.JRadioButton rbDinner;
    private javax.swing.JRadioButton rbLunch;
    private javax.swing.JTextField search;
    private javax.swing.JTextField searchAdmin;
    private javax.swing.JList searchAdminList;
    private javax.swing.JPanel searchAdminPanel;
    private javax.swing.JList searchList;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JSeparator separator;
    private javax.swing.JTextField setAdminName;
    private javax.swing.JTextField street;
    private javax.swing.JLabel street1;
    private javax.swing.JTextField street2;
    private javax.swing.JTabbedPane studentPanel;
    private javax.swing.JPanel updateAdminPanel;
    private javax.swing.JPanel updatePanel;
    private javax.swing.JLabel userAdmin;
    private javax.swing.JTextField username;
    private javax.swing.JInternalFrame validationsFrame;
    private javax.swing.JList validationsJlist;
    private javax.swing.JPanel visualizeAdminPanel;
    private javax.swing.JPanel visualizePanel;
    private javax.swing.JInternalFrame warningFrame;
    private javax.swing.JInternalFrame warningLogFrame;
    private javax.swing.JInternalFrame warningSearchFrame;
    // End of variables declaration//GEN-END:variables
}
