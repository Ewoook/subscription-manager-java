package gui;

import model.*;
import logic.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AppGUI extends JFrame {
    private JTextField nameField;
    private JTextField costField;

    private JComboBox<Currency> currencyBox;
    private JComboBox<Category> categoryBox;
    private JComboBox<Period > periodBox;
    private JSpinner dateSpinner;

    private JComboBox<Object> filterCategoryBox;
    private static final String[] SORTOPTIONS = {"Name (A-Z)", "Name (Z-A)", "Cost (Ascending)", "Cost (Descending)"};
    private JComboBox<String> sortBox;
    private JPanel listPanel;
    private JLabel summaryLabel;

    private JTextField searchField;

    private AppLogic logic = new AppLogic();
    private double totalAnnualCostPLN = 0;



    public AppGUI()
    {
        //ustawiamy tytuł, rozmiar oraz layout dla aplikacji
        setTitle("Subscription Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout(10,10));

        //tworzymy 3 panele na których będzie opierała się nasza aplkiacja i dodajemy je do ramki

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryLabel = new JLabel("Total annual cost: 0.00 PLN");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bottomPanel.add(summaryLabel);
        add(bottomPanel, BorderLayout.SOUTH);


        for(Subscription sub : logic.getSubscriptions())
        {
            addSubscriptionRow(sub);
        }
        refreshList();


        setVisible(true);
    }

    public JPanel createHeaderPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7,2,5,5)); //tworzymy panel z 7 wierszami i 2 kolumnami
        panel.setBorder(BorderFactory.createTitledBorder("Subscription")); //górna granica panelu z tytułem

        //dodajemy poszczególne pola ze skladowymi klasy model.Subscription

        panel.add(new JLabel("Subscription name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Cost:"));
        costField = new JTextField();
        panel.add(costField);

        panel.add(new JLabel("Currency:"));
        currencyBox = new JComboBox<>(Currency.values()); //lista rozwijana z wyborem waluty
        panel.add(currencyBox);

        panel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>(Category.values()); //lista rozwijana z wyborem kategorii
        panel.add(categoryBox);

        panel.add(new JLabel("Settlement period:"));
        periodBox = new JComboBox<>(Period.values()); //lista rozwijana z wyborem okresu rozliczeniowego
        panel.add(periodBox);

        panel.add(new JLabel("First payment date:"));
        SpinnerDateModel model =  new SpinnerDateModel(); //SpinnerDateModel mowi Jspinnerowi ze przewija on daty
        dateSpinner = new JSpinner(model); //pole tekstowe ze strzalkami do wybierania wartosci poprzez przewijanie
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(editor);
        panel.add(dateSpinner);

        JButton addButton = new JButton("Add subscription"); //przycisk do dodania subskrypcji
        panel.add(new JLabel(""));
        panel.add(addButton);

        addButton.addActionListener(e -> addSubscription()); //przy kliknieciu przycisku wywolujemy metode addSubscription

        return panel;


    }

    public JPanel createCenterPanel()
    {
        //dzielimy CenterPanel na 2 panele (1. z mozliwoscia filtrowania i sortowania, 2. wyswietlajacy liste abonamentow
        JPanel centerRoot = new JPanel(new BorderLayout());

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //odzielamy te 2 panele linia za pomoca MatteBorder
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        toolbarPanel.setBackground(new Color(245, 245, 245));


        //tworzymy liste kategorii (bierzemy je z enuma model.Category oraz dodajemy sztuczna kategorie All)
        toolbarPanel.add(new JLabel("Filter:"));
        List<Object> categories = new ArrayList<>();
        categories.add("ALL");
        for (Category c : Category.values()) categories.add(c);

        filterCategoryBox = new JComboBox<>(categories.toArray()); //dodajemy liste kategorii do listy rozwijanej
        filterCategoryBox.addActionListener(e -> refreshList()); //jesli uzytkownik zmieni wyswietlana kategorie musimy wywolac metode refresh
        toolbarPanel.add(filterCategoryBox);

        toolbarPanel.add(new JLabel(" Szukaj:"));
        searchField = new JTextField(10); // Szerokość na 10 znaków

        // Ten kod sprawia, że lista odświeża się przy każdym wpisanym znaku!
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
        });

        toolbarPanel.add(searchField);

        toolbarPanel.add(new JLabel(" Sort:"));
        sortBox = new JComboBox<>(SORTOPTIONS);
        sortBox.addActionListener(e -> refreshList());
        toolbarPanel.add(sortBox);

        centerRoot.add(toolbarPanel, BorderLayout.NORTH);

        //tworzymy panel w ktorym elementy sa ukladanie pionowo, jeden pod drugim
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        //tworzymy dodatkowy panel z ukladem BorderLayout i ustawiamy go na NORTH aby lista byla przycisnieta do gory
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(listPanel, BorderLayout.NORTH);

        //tworzymy okno z suwakami do ktorego wkladamy nasza liste(we wrapperze)
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); //zamiast o 1px scroll teraz przewija o jedna linijke tekstu 16px

        centerRoot.add(scrollPane, BorderLayout.CENTER);

        return centerRoot;
    }

    private void addSubscriptionRow(Subscription sub) {

        //Subskrybcje dodajemy jako osobne panele aby zmiescic przycisk do usuwania z prawej strony

        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));

        rowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); //odzdzielamy kazda subskrypcje linią
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); //ustawiamy wielkosc jednego panelu z subskrypcja
        rowPanel.setBackground(Color.WHITE);

        JLabel textLabel = new JLabel(sub.toString()); //korzystamy z polimorfii i wykorzystujemy nadpisana metode toStirng()

        //Monospaced to jedyny rodzaj czcionki, w którym każda litera, cyfra i spacja zajmują dokładnie tyle samo miejsca w poziomie. (tekst sie nie rozjedzie)
        textLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rowPanel.add(textLabel, BorderLayout.CENTER);

        //tworzymy przycisk ktory umozliwa usuniecie subskrybcji
        JButton deleteButton = new JButton("X");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));

        deleteButton.addActionListener(e -> {
            //usuwamy subskrypcje z listy oraz zmieniamy podsumowanie wyswietlane w prawym dolnym rogu (w zł więc trzeba zastosowac metode exchange)
            logic.removeSub(sub);
            refreshList();

        });

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER)); //tworzymy pomocniczy panel button container aby wysrodkowac jego pozycje
        buttonContainer.setOpaque(false);
        buttonContainer.add(deleteButton);

        rowPanel.add(buttonContainer, BorderLayout.EAST); //dodajemy buttonContainer do wiersza z subskrypcja z prawej strony

        listPanel.add(rowPanel);

        //po dodaniu wszystkiego wywolujemy te 2 metody aby Swing przeliczył układ i przerysował listPanel (nie jest to automatyczne)
        listPanel.revalidate();
        listPanel.repaint();
    }

    public void addSubscription()
    {
        //metoda służy do ściągnięcia informacji wpisanych przez użytkownika i dodania ich do listy zarówno graficznej jak i tej w logice programu

        try
        {
            String name = nameField.getText();
            double cost = Double.parseDouble(costField.getText().replace(",", ".")); //zmieniamy "," na "." zeby nie wyskoczył bład przy złym formacie
            if(cost < 0)
            {
                throw new InvalidCostException("Cost cannot be a negative number!"); //wywołujemy spersonalizowany wyjątek
            }
            Currency currency = (Currency) currencyBox.getSelectedItem();
            Category category = (Category) categoryBox.getSelectedItem();
            Period period = (Period) periodBox.getSelectedItem();
            Date dateFromSpinner = (Date) dateSpinner.getValue(); //dateFromSpinner to liczba milisekund od 1970 (wazne przy nastepnej linijce)
            //toInstant() ustawia nasza date w konkretnym miejscu w czasie, atZone(...) pobiera strefe czasowa uzytkownika, tolocalDate() odcina godzine oraz strefe czasowa
            LocalDate startDate = dateFromSpinner.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            assert period != null; //zabezpieczenie przy błedzie programu
            Subscription sub = switch (period) {
                case DAILY -> new DailySub(name, cost, currency, category, startDate);
                case WEEKLY -> new WeeklySub(name, cost, currency, category, startDate);
                case MONTHLY -> new MonthlySub(name, cost, currency, category, startDate);
                case YEARLY -> new YearlySub(name, cost, currency, category, startDate);
            };

            logic.addSub(sub);
            refreshList();

            nameField.setText("");
            costField.setText("");
        }
        catch(NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,"ERROR! Cost must be a number." ,"Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (InvalidCostException ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR! Cost cannot be a negative number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    private void updateSummaryLabel(double amountInPLN) {


        summaryLabel.setText(String.format("Total annual cost: %.2f PLN", (Double) totalAnnualCostPLN));
    }

    public void refreshList()
    {
        totalAnnualCostPLN = 0;
        listPanel.removeAll(); //usuwamy wszystkie elementy listy wyswietlanej ale jest ona zachowana w logice

        Object selectedCat = filterCategoryBox.getSelectedItem(); //patrzymy czy uzytkownik filtrowal po kategorii

        String searchText = searchField.getText().trim().toLowerCase();

        List<Subscription> subscriptions = logic.getSubscriptions();
        List<Subscription> displayList = new ArrayList<>(subscriptions);

        if(!"ALL".equals(selectedCat))
        {
            //usuwamy wszystkie elementy ktore nie pasuja do wybranej kategorii
            displayList.removeIf(sub -> sub.getCategory() != selectedCat);

        }

        if (!searchText.isEmpty()) {
            displayList.removeIf(sub -> {
                // sprawdzamy, czy nazwa subskrypcji zawiera wpisaną frazę
                String subName = sub.getName().toLowerCase();
                return !subName.contains(searchText); // jeśli NIE zawiera to usuń z listy
            });

        }

        Comparator<Subscription> comparator = null; //comparator pomaga w sortowaniu, ustawia obiekty w danej kolejnosci
        int selectedSortIndex = sortBox.getSelectedIndex();

        comparator = switch (selectedSortIndex) {
            case 0 -> Comparator.comparing(Subscription::getName, String.CASE_INSENSITIVE_ORDER); //ustawia alfabetycznie (A-Z)
            case 1 -> Comparator.comparing(Subscription::getName, String.CASE_INSENSITIVE_ORDER.reversed()); //(Z-A)
            case 2 -> Comparator.comparingDouble(s -> logic.exchange(s.getAnnualCost(), s.getCurrency(), Currency.PLN));  //Rosnąco
            case 3 -> Comparator.comparingDouble((Subscription s) -> logic.exchange(s.getAnnualCost(), s.getCurrency(), Currency.PLN)).reversed(); //Malejąco
            default -> throw new IllegalStateException("Unexpected value: " + selectedSortIndex); //zabezpieczenie
        };

        displayList.sort(comparator); //sortujemy liste subskrypcji

        for (Subscription sub : displayList) {
            addSubscriptionRow(sub);

            totalAnnualCostPLN += logic.exchange(sub.getAnnualCost(), sub.getCurrency(), Currency.PLN);
        }

        updateSummaryLabel(totalAnnualCostPLN);
        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(AppGUI::new);
    }
}
