package winthorDb.forms;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

// Web browser
public class MiniBrowser extends JFrame
        implements HyperlinkListener {

    // Estes s�o os bot�es para itera��o com a lista de p�ginhas
    private JButton backButton, forwardButton;

    // Campo para digitar o endere�o.
    private JTextField locationTextField;

    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;

    // Lista de p�ginas ja visitadas pelo browser.
    private ArrayList pageList = new ArrayList();

    // Construtor do Mini Web Browser.
    public MiniBrowser() {
        // Configura t�tulo da aplica��o.
        super("Mini Browser");

        // Configura tamanho da janela.
        setSize(800, 600);

        // Manipulador de eventos.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });

        // Configura o menu file.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Configura os bot�es do painel.
        JPanel buttonPanel = new JPanel();
        backButton = new JButton("< Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionBack();
            }
        });
        backButton.setEnabled(false);
        buttonPanel.add(backButton);
        forwardButton = new JButton("Forward >");
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionForward();
            }
        });
        forwardButton.setEnabled(false);
        buttonPanel.add(forwardButton);
        locationTextField = new JTextField(35);
        locationTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionGo();
                }
            }
        });
        buttonPanel.add(locationTextField);
        JButton goButton = new JButton("GO");
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionGo();
            }
        });
        buttonPanel.add(goButton);

        // Configura o espa�o da p�gina.
        displayEditorPane = new JEditorPane();
        //displayEditorPane.setContentType("text/html");
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);
        displayEditorPane.addHyperlinkListener(this);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);
    }

    // Sa�da do programa.
    private void actionExit() {
        System.exit(0);
    }

    // Este m�todo permite a volta para a �ltima p�gina visitada.
    private void actionBack() {
        URL currentUrl = displayEditorPane.getPage();
        int pageIndex = pageList.indexOf(currentUrl.toString());
        try {
            showPage(
                    new URL((String) pageList.get(pageIndex - 1)), false);
        } catch (MalformedURLException e) {
        }
    }

    // M�todo que permite  voltar para a p�gina atual.
    private void actionForward() {
        URL currentUrl = displayEditorPane.getPage();
        int pageIndex = pageList.indexOf(currentUrl.toString());
        try {
            showPage(
                    new URL((String) pageList.get(pageIndex + 1)), false);
        } catch (MalformedURLException e) {
        }
    }

    // Carrrega e apresenta a p�gina especificada no campo de texto.
    private void actionGo() {
        URL verifiedUrl = verifyUrl(locationTextField.getText());
        if (verifiedUrl != null) {
            showPage(verifiedUrl, true);
        } else {
            showError("Invalid URL");
        }
    }

    // Apresenta mensagem de erro em uma caixa de di�logo.
    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Verifica o formato da URL.
    private URL verifyUrl(String url) {
        // S� permite URLs HTTP.
        if (!url.toLowerCase().startsWith("http://")) {
            return null;
        }

        // Verifica o formato da URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }

        return verifiedUrl;
    }

    /* Mostra p�gina solicitada e adiciona ela � lista de p�ginas visitadas. */
    private void showPage(URL pageUrl, boolean addToList) {
        // Modifica o cursor do mouse. 
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Obt�m URL da p�gina mostrada atualmente.
            URL currentUrl = displayEditorPane.getPage();

            // Carrega e exibe p�gina desejada.
            displayEditorPane.setPage(pageUrl);

            // Obt�m URL da nova p�gina sendo exibida.
            URL newUrl = displayEditorPane.getPage();

            // Adiciona p�gina � lista de p�ginhas visitadas.
            if (addToList) {
                int listSize = pageList.size();
                if (listSize > 0) {
                    int pageIndex
                            = pageList.indexOf(currentUrl.toString());
                    if (pageIndex < listSize - 1) {
                        for (int i = listSize - 1; i > pageIndex; i--) {
                            pageList.remove(i);
                        }
                    }
                }
                pageList.add(newUrl.toString());
            }

            // Atualiza o campo de entrada da URL com a p�gina atual.
            locationTextField.setText(newUrl.toString());

            // Atualiza os bot�es.
            updateButtons();
        } catch (IOException e) {
            // Apresenta mensagem de erro.
            showError("Unable to load page");
        } finally {
            // Retorna para o cursor padr�o.
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /* Atualiza os bot�es back e forward baseado na p�gina que est� sendo apresentada exibida. */
    private void updateButtons() {
        if (pageList.size() < 2) {
            backButton.setEnabled(false);
            forwardButton.setEnabled(false);
        } else {
            URL currentUrl = displayEditorPane.getPage();
            int pageIndex = pageList.indexOf(currentUrl.toString());
            backButton.setEnabled(pageIndex > 0);
            forwardButton.setEnabled(
                    pageIndex < (pageList.size() - 1));
        }
    }

    // Manipulador de hyperlink's clicados.
    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent
                        = (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document
                        = (HTMLDocument) displayEditorPane.getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
                showPage(event.getURL(), true);
            }
        }
    }

    // Executa o Mini Browser.
    public static void main(String[] args) {
        MiniBrowser browser = new MiniBrowser();
        browser.show();
    }
}
