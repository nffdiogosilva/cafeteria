
package pt.uac.cafeteria.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;

/**
 * 
 * Represents the Front Office user interface.
 */
public class Frontend extends javax.swing.JFrame {

    private static int year = Calendar.getInstance().get(Calendar.YEAR);
    private static int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private static int day = Calendar.getInstance().get(Calendar.DATE);
    
    /** Creates new form Frontend */
    public Frontend() {
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);
        
        menuPanel.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        time = new javax.swing.ButtonGroup();
        dish = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        lblNumber = new javax.swing.JLabel();
        number = new javax.swing.JTextField();
        lblPinCode = new javax.swing.JLabel();
        pinCode = new javax.swing.JPasswordField();
        confirm = new javax.swing.JButton();
        logo = new javax.swing.JLabel();
        lblFrontBK = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnBuyTicket = new javax.swing.JButton();
        btnCheckBalance = new javax.swing.JButton();
        btnChangePinCode = new javax.swing.JButton();
        brnChangeEmail = new javax.swing.JButton();
        btnTerminate = new javax.swing.JButton();
        panelBuyTicket = new javax.swing.JPanel();
        ifWarning = new javax.swing.JInternalFrame();
        lblMoreTickets1 = new javax.swing.JLabel();
        btnYesCancel = new javax.swing.JButton();
        btnNoCancel = new javax.swing.JButton();
        ifPurchaseSuccess = new javax.swing.JInternalFrame();
        success = new javax.swing.JLabel();
        purchaseOk = new javax.swing.JButton();
        spSummary = new javax.swing.JScrollPane();
        panelSummary = new javax.swing.JPanel();
        lblPurchaseDate = new javax.swing.JLabel();
        lblPurchaseDateText = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblPriceText = new javax.swing.JLabel();
        lpMeal1 = new javax.swing.JLayeredPane();
        lblMealDate = new javax.swing.JLabel();
        lblMealTime = new javax.swing.JLabel();
        lblMealDish = new javax.swing.JLabel();
        ifPrompt = new javax.swing.JInternalFrame();
        lblMoreTickets = new javax.swing.JLabel();
        btnYesTickets = new javax.swing.JButton();
        btnNoTickets = new javax.swing.JButton();
        chooseDay = new javax.swing.JPanel();
        cbYearChoice = new javax.swing.JComboBox();
        cbMonthChoice = new javax.swing.JComboBox();
        cbDayChoice = new javax.swing.JComboBox();
        rbLunch = new javax.swing.JRadioButton();
        rbDinner = new javax.swing.JRadioButton();
        showMeal = new javax.swing.JPanel();
        lblChooseDish = new javax.swing.JLabel();
        rbMeat = new javax.swing.JRadioButton();
        rbFish = new javax.swing.JRadioButton();
        rbVegetarian = new javax.swing.JRadioButton();
        lblSoup = new javax.swing.JLabel();
        lblMeat = new javax.swing.JLabel();
        lblFish = new javax.swing.JLabel();
        lblVegetarian = new javax.swing.JLabel();
        lblDessert = new javax.swing.JLabel();
        lblSoupText = new javax.swing.JLabel();
        lblMeatText = new javax.swing.JLabel();
        lblFishText = new javax.swing.JLabel();
        lblVegetarianText = new javax.swing.JLabel();
        lblDessertText = new javax.swing.JLabel();
        btnPurchase = new javax.swing.JButton();
        btnConfirmTicket = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblLogo1 = new javax.swing.JLabel();
        lblFrontBK1 = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cafeteria");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        mainPanel.setMinimumSize(new java.awt.Dimension(800, 600));
        mainPanel.setLayout(null);

        lblNumber.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblNumber.setText("Número de Conta");
        mainPanel.add(lblNumber);
        lblNumber.setBounds(355, 150, 110, 14);

        number.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        number.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        mainPanel.add(number);
        number.setBounds(350, 170, 110, 30);

        lblPinCode.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblPinCode.setText("Código de Acesso");
        mainPanel.add(lblPinCode);
        lblPinCode.setBounds(355, 240, 110, 14);
        mainPanel.add(pinCode);
        pinCode.setBounds(350, 265, 110, 30);

        confirm.setText("Confirmar");
        confirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                confirmMouseReleased(evt);
            }
        });
        mainPanel.add(confirm);
        confirm.setBounds(360, 343, 90, 30);

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/logo.png"))); // NOI18N
        mainPanel.add(logo);
        logo.setBounds(355, 390, 100, 100);

        lblFrontBK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/frontBK.png"))); // NOI18N
        mainPanel.add(lblFrontBK);
        lblFrontBK.setBounds(0, 0, 939, 683);

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );

        mainPanel.add(loginPanel);
        loginPanel.setBounds(300, 100, 200, 400);

        menuPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        menuPanel.setLayout(null);

        panelButtons.setOpaque(false);

        btnBuyTicket.setText("Comprar Senha");
        btnBuyTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnBuyTicketMouseReleased(evt);
            }
        });

        btnCheckBalance.setText("Consultar Saldo");

        btnChangePinCode.setText("Alterar Código de Acesso");

        brnChangeEmail.setText("Alterar e-mail");

        btnTerminate.setText("Terminar");
        btnTerminate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnTerminateMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panelButtonsLayout = new javax.swing.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuyTicket, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnCheckBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnChangePinCode, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(brnChangeEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnTerminate, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnBuyTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(btnCheckBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnChangePinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(brnChangeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(307, 307, 307)
                .addComponent(btnTerminate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        menuPanel.add(panelButtons);
        panelButtons.setBounds(0, 0, 200, 600);

        panelBuyTicket.setOpaque(false);
        panelBuyTicket.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ifWarning.setTitle("Aviso");
        ifWarning.setVisible(true);

        lblMoreTickets1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblMoreTickets1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMoreTickets1.setText("Tem a certeza que quer Cancelar?");

        btnYesCancel.setText("Sim");
        btnYesCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnYesCancelMouseReleased(evt);
            }
        });

        btnNoCancel.setText("Não");
        btnNoCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnNoCancelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ifWarningLayout = new javax.swing.GroupLayout(ifWarning.getContentPane());
        ifWarning.getContentPane().setLayout(ifWarningLayout);
        ifWarningLayout.setHorizontalGroup(
            ifWarningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ifWarningLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(btnYesCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNoCancel)
                .addGap(43, 43, 43))
            .addComponent(lblMoreTickets1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
        );
        ifWarningLayout.setVerticalGroup(
            ifWarningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifWarningLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblMoreTickets1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ifWarningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNoCancel)
                    .addComponent(btnYesCancel))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panelBuyTicket.add(ifWarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 220, 160));

        ifPurchaseSuccess.setTitle("Informação");
        ifPurchaseSuccess.setVisible(true);

        success.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        success.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        success.setText("Compra efectuada com sucesso!");

        purchaseOk.setText("OK");
        purchaseOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                purchaseOkMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ifPurchaseSuccessLayout = new javax.swing.GroupLayout(ifPurchaseSuccess.getContentPane());
        ifPurchaseSuccess.getContentPane().setLayout(ifPurchaseSuccessLayout);
        ifPurchaseSuccessLayout.setHorizontalGroup(
            ifPurchaseSuccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(success, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
            .addGroup(ifPurchaseSuccessLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(purchaseOk, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ifPurchaseSuccessLayout.setVerticalGroup(
            ifPurchaseSuccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifPurchaseSuccessLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(success, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(purchaseOk)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panelBuyTicket.add(ifPurchaseSuccess, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 220, 160));

        spSummary.setBorder(null);
        spSummary.setToolTipText("");

        panelSummary.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));
        panelSummary.setOpaque(false);
        panelSummary.setPreferredSize(new java.awt.Dimension(300, 350));

        lblPurchaseDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPurchaseDate.setText("Data da Compra:");

        lblPrice.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPrice.setText("Preço Total:");

        lblPriceText.setText("3€");

        lpMeal1.setBackground(new java.awt.Color(102, 102, 102));
        lpMeal1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblMealDate.setText("10/1/2012");
        lblMealDate.setBounds(10, 10, 70, 20);
        lpMeal1.add(lblMealDate, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblMealTime.setText("Almoço");
        lblMealTime.setBounds(10, 40, 70, 20);
        lpMeal1.add(lblMealTime, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblMealDish.setText("Carne");
        lblMealDish.setBounds(10, 70, 90, 20);
        lpMeal1.add(lblMealDish, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout panelSummaryLayout = new javax.swing.GroupLayout(panelSummary);
        panelSummary.setLayout(panelSummaryLayout);
        panelSummaryLayout.setHorizontalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lpMeal1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPurchaseDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPurchaseDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        panelSummaryLayout.setVerticalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblPurchaseDateText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPurchaseDate, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(lpMeal1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );

        spSummary.setViewportView(panelSummary);

        panelBuyTicket.add(spSummary, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 380));

        ifPrompt.setTitle("Informação");
        ifPrompt.setVisible(true);

        lblMoreTickets.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMoreTickets.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMoreTickets.setText("Deseja adquirir mais senhas?");

        btnYesTickets.setText("Sim");
        btnYesTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnYesTicketsMouseReleased(evt);
            }
        });

        btnNoTickets.setText("Não");
        btnNoTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnNoTicketsMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ifPromptLayout = new javax.swing.GroupLayout(ifPrompt.getContentPane());
        ifPrompt.getContentPane().setLayout(ifPromptLayout);
        ifPromptLayout.setHorizontalGroup(
            ifPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ifPromptLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(btnYesTickets)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNoTickets)
                .addGap(43, 43, 43))
            .addComponent(lblMoreTickets, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
        );
        ifPromptLayout.setVerticalGroup(
            ifPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifPromptLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblMoreTickets, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ifPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNoTickets)
                    .addComponent(btnYesTickets))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panelBuyTicket.add(ifPrompt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 220, 160));

        chooseDay.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolha a data e a refeição", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));
        chooseDay.setMinimumSize(new java.awt.Dimension(400, 400));
        chooseDay.setPreferredSize(new java.awt.Dimension(333, 155));
        chooseDay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbYearChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2011", "2012", "2013", "2014", "2015", "2016", "2017" }));
        chooseDay.add(cbYearChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        cbMonthChoice.setMaximumRowCount(12);
        cbMonthChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
        chooseDay.add(cbMonthChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        cbDayChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        chooseDay.add(cbDayChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        time.add(rbLunch);
        rbLunch.setText("Almoço");
        rbLunch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbLunchMouseReleased(evt);
            }
        });
        chooseDay.add(rbLunch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, -1));

        time.add(rbDinner);
        rbDinner.setText("Jantar");
        rbDinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbDinnerMouseReleased(evt);
            }
        });
        chooseDay.add(rbDinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 123, -1, -1));

        panelBuyTicket.add(chooseDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, -1));

        showMeal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MENU", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));
        showMeal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblChooseDish.setText("Faça a sua escolha:");
        showMeal.add(lblChooseDish, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        dish.add(rbMeat);
        rbMeat.setText("Carne");
        rbMeat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbMeatMouseReleased(evt);
            }
        });
        showMeal.add(rbMeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        dish.add(rbFish);
        rbFish.setText("Peixe");
        showMeal.add(rbFish, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, -1, -1));

        dish.add(rbVegetarian);
        rbVegetarian.setText("Vegetariano");
        showMeal.add(rbVegetarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, -1, -1));

        lblSoup.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblSoup.setText("Sopa:");
        showMeal.add(lblSoup, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        lblMeat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblMeat.setText("Carne:");
        showMeal.add(lblMeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        lblFish.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFish.setText("Peixe:");
        showMeal.add(lblFish, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 20));

        lblVegetarian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblVegetarian.setText("Vegetariano:");
        showMeal.add(lblVegetarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        lblDessert.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDessert.setText("Sobremesa:");
        showMeal.add(lblDessert, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, 20));
        showMeal.add(lblSoupText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 290, 20));
        showMeal.add(lblMeatText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 290, 20));
        showMeal.add(lblFishText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 290, 20));
        showMeal.add(lblVegetarianText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 290, 20));
        showMeal.add(lblDessertText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 290, 20));

        panelBuyTicket.add(showMeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 420, 260));

        btnPurchase.setText("Comprar");
        btnPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseReleased(evt);
            }
        });
        panelBuyTicket.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 490, 80, 30));

        btnConfirmTicket.setText("OK");
        btnConfirmTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnConfirmTicketMouseReleased(evt);
            }
        });
        panelBuyTicket.add(btnConfirmTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 490, 80, 32));

        btnCancel.setText("Cancelar");
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCancelMouseReleased(evt);
            }
        });
        panelBuyTicket.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 490, 80, 30));

        menuPanel.add(panelBuyTicket);
        panelBuyTicket.setBounds(250, 25, 440, 522);

        lblLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/logo.png"))); // NOI18N
        menuPanel.add(lblLogo1);
        lblLogo1.setBounds(675, 475, 100, 100);

        lblFrontBK1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/uac/cafeteria/ui/images/frontBK.png"))); // NOI18N
        menuPanel.add(lblFrontBK1);
        lblFrontBK1.setBounds(0, 0, 939, 683);

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        menuPanel.add(separator);
        separator.setBounds(205, 0, 10, 611);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmMouseReleased
        mainPanel.setVisible(false);
        menuPanel.setVisible(true);
        panelBuyTicket.setVisible(false);
        
        chooseDay.setVisible(false);
        btnBuyTicket.setEnabled(true);
        btnCheckBalance.setEnabled(true);
        btnChangePinCode.setEnabled(true);
        brnChangeEmail.setEnabled(true);
    }//GEN-LAST:event_confirmMouseReleased

    private void btnTerminateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTerminateMouseReleased
        menuPanel.setVisible(false);
        mainPanel.setVisible(true);
    }//GEN-LAST:event_btnTerminateMouseReleased

    private void btnBuyTicketMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuyTicketMouseReleased
        panelBuyTicket.setVisible(true);
        chooseDay.setVisible(true);
        showMeal.setVisible(false);
        btnConfirmTicket.setVisible(true);
        btnConfirmTicket.setEnabled(false);
        btnCancel.setVisible(true);
        ifPrompt.setVisible(false);
        spSummary.setVisible(false);
        btnPurchase.setVisible(false);
        time.clearSelection();
        dish.clearSelection();
        ifPurchaseSuccess.setVisible(false);
        ifWarning.setVisible(false);
        
        btnBuyTicket.setEnabled(false);
        btnCheckBalance.setEnabled(true);
        btnChangePinCode.setEnabled(true);
        brnChangeEmail.setEnabled(true);
    }//GEN-LAST:event_btnBuyTicketMouseReleased

    private void btnCancelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseReleased
        ifWarning.setVisible(true);
    }//GEN-LAST:event_btnCancelMouseReleased

    private void rbLunchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbLunchMouseReleased
        showMeal.setVisible(true);
        lblSoupText.setText("Caldo Verde");
        lblMeatText.setText("Bifes");
        lblFishText.setText("Chicharros");
        lblVegetarianText.setText("Salada");
        lblDessertText.setText("Mousse");        
    }//GEN-LAST:event_rbLunchMouseReleased

    private void rbMeatMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbMeatMouseReleased
        btnConfirmTicket.setEnabled(true);
    }//GEN-LAST:event_rbMeatMouseReleased

    private void btnConfirmTicketMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmTicketMouseReleased
        chooseDay.setVisible(false);
        showMeal.setVisible(false);
        ifPrompt.setVisible(true);
        btnConfirmTicket.setEnabled(false);
    }//GEN-LAST:event_btnConfirmTicketMouseReleased

    private void btnYesTicketsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnYesTicketsMouseReleased
        ifPrompt.setVisible(false);
        chooseDay.setVisible(true);
        showMeal.setVisible(false);
        time.clearSelection();
        dish.clearSelection();
    }//GEN-LAST:event_btnYesTicketsMouseReleased

    private void btnNoTicketsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNoTicketsMouseReleased
        ifPrompt.setVisible(false);
        chooseDay.setVisible(false);
        showMeal.setVisible(false);
        spSummary.setVisible(true);
        btnPurchase.setVisible(true);
        btnConfirmTicket.setVisible(false);
        
        lblPurchaseDateText.setText(""+day+"/"+month+"/"+year);
    }//GEN-LAST:event_btnNoTicketsMouseReleased

    private void btnPurchaseMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseReleased
        spSummary.setVisible(false);
        btnPurchase.setVisible(false);
        btnCancel.setVisible(false);
        ifPurchaseSuccess.setVisible(true);
    }//GEN-LAST:event_btnPurchaseMouseReleased

    private void purchaseOkMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseOkMouseReleased
        ifPurchaseSuccess.setVisible(false);
        btnBuyTicket.setEnabled(true);
    }//GEN-LAST:event_purchaseOkMouseReleased

    private void rbDinnerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbDinnerMouseReleased
        showMeal.setVisible(true);
        lblSoupText.setText("Sopa de Cenoura");
        lblMeatText.setText("Carne Assada");
        lblFishText.setText("Salmão");
        lblVegetarianText.setText("Massa");
        lblDessertText.setText("Salada de Fruta"); 
    }//GEN-LAST:event_rbDinnerMouseReleased

    private void btnYesCancelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnYesCancelMouseReleased
        chooseDay.setVisible(false);
        showMeal.setVisible(false);
        btnConfirmTicket.setVisible(false);
        btnCancel.setVisible(false);
        ifPrompt.setVisible(false);
        spSummary.setVisible(false);
        btnPurchase.setVisible(false);
        ifWarning.setVisible(false);
        
        btnBuyTicket.setEnabled(true);
    }//GEN-LAST:event_btnYesCancelMouseReleased

    private void btnNoCancelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNoCancelMouseReleased
        ifWarning.setVisible(false);
    }//GEN-LAST:event_btnNoCancelMouseReleased

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
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Frontend().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnChangeEmail;
    private javax.swing.JButton btnBuyTicket;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnChangePinCode;
    private javax.swing.JButton btnCheckBalance;
    private javax.swing.JButton btnConfirmTicket;
    private javax.swing.JButton btnNoCancel;
    private javax.swing.JButton btnNoTickets;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnTerminate;
    private javax.swing.JButton btnYesCancel;
    private javax.swing.JButton btnYesTickets;
    private javax.swing.JComboBox cbDayChoice;
    private javax.swing.JComboBox cbMonthChoice;
    private javax.swing.JComboBox cbYearChoice;
    private javax.swing.JPanel chooseDay;
    private javax.swing.JButton confirm;
    private javax.swing.ButtonGroup dish;
    private javax.swing.JInternalFrame ifPrompt;
    private javax.swing.JInternalFrame ifPurchaseSuccess;
    private javax.swing.JInternalFrame ifWarning;
    private javax.swing.JLabel lblChooseDish;
    private javax.swing.JLabel lblDessert;
    private javax.swing.JLabel lblDessertText;
    private javax.swing.JLabel lblFish;
    private javax.swing.JLabel lblFishText;
    private javax.swing.JLabel lblFrontBK;
    private javax.swing.JLabel lblFrontBK1;
    private javax.swing.JLabel lblLogo1;
    private javax.swing.JLabel lblMealDate;
    private javax.swing.JLabel lblMealDish;
    private javax.swing.JLabel lblMealTime;
    private javax.swing.JLabel lblMeat;
    private javax.swing.JLabel lblMeatText;
    private javax.swing.JLabel lblMoreTickets;
    private javax.swing.JLabel lblMoreTickets1;
    private javax.swing.JLabel lblNumber;
    private javax.swing.JLabel lblPinCode;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblPriceText;
    private javax.swing.JLabel lblPurchaseDate;
    private javax.swing.JLabel lblPurchaseDateText;
    private javax.swing.JLabel lblSoup;
    private javax.swing.JLabel lblSoupText;
    private javax.swing.JLabel lblVegetarian;
    private javax.swing.JLabel lblVegetarianText;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel logo;
    private javax.swing.JLayeredPane lpMeal1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTextField number;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelBuyTicket;
    private javax.swing.JPanel panelSummary;
    private javax.swing.JPasswordField pinCode;
    private javax.swing.JButton purchaseOk;
    private javax.swing.JRadioButton rbDinner;
    private javax.swing.JRadioButton rbFish;
    private javax.swing.JRadioButton rbLunch;
    private javax.swing.JRadioButton rbMeat;
    private javax.swing.JRadioButton rbVegetarian;
    private javax.swing.JSeparator separator;
    private javax.swing.JPanel showMeal;
    private javax.swing.JScrollPane spSummary;
    private javax.swing.JLabel success;
    private javax.swing.ButtonGroup time;
    // End of variables declaration//GEN-END:variables
}
