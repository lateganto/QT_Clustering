package client;

//<APPLET CODE="QT" WIDTH="500" HEIGHT="380"> 
//<PARAM NAME="ServerIP" VALUE="localhost">
//<PARAM NAME="Port" VALUE="8089">
//</APPLET>

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * <p> Title: QT </p>
 * <p> Class description: definisce una applet eseguibile in un browser web. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
@SuppressWarnings("serial")
public class QT extends JApplet{

	/**
	 * Stream per la comunicazione in uscita con il server.
	 */
	private ObjectOutputStream out;
	/**
	 * Stream per la comunicazione in ingresso con il server.
	 */
	private ObjectInputStream in;

	/**
	 * <p> Title: TabbedPane </p>
	 * <p> Class description: classe TabbedPane che estende JPanel ed � inner class di QT. 
	 * 						  Rappresenta un pannello che include due JPanelCluster switchabili. </p>
	 * @author Gentile, Lategano, Visaggi
	 *
	 */
	private class TabbedPane extends JPanel{
		/**
		 * Pannello di tipo JPanelCluster.
		 */
		private JPanelCluster panelDB; 
		/**
		 * Pannello di tipo JPanelCluster. 
		 */
		private JPanelCluster panelFile;
		
		/**
		 * <p> Title: JPanelCluster </p>
		 * <p> Class description: definisce un pannello generico il cui layout verr� usato sia per panelDb che per
		 * 						  panelFile. </p>
		 * @author Gentile, Lategano, Visaggi
		 *
		 */
		private class JPanelCluster extends JPanel{
			/**
			 * Casella di testo per l'inserimento del nome della tabella.
			 */
			private JTextField tableText = new JTextField(20); 
			/**
			 * Casella di testo per l'inserimento del parametro da passare (raggio del cluster).
			 */
			private JTextField parameterText = new JTextField(10); 
			/**
			 * Area di testo in cui viene visualizzato il risultato della computazione.
			 */
			private JTextArea clusterOutput = new JTextArea(); 
			/**
			 * Bottone per avviare l'esecuzione.
			 */
			private JButton executeButton;

			/**
			 * Inizializza il pannello con i vari componenti suddivisi per sottopannelli nord, sud e centrale.
			 * Il pannello superiore contiene l'etichetta e la casella di testo per inserire il nome della tabella e il raggio.
			 * Il pannello centrale � composto da un'unica area di testo estesa per visualizzare il risultato della computazione.
			 * Il pannello inferiore comprende solo il bottone per avviare la esecuzione.
			 * Viene infine aggiunto l'ascoltatore a al bottone executeButton.
			 * @param buttonName etichetta per il bottone.
			 * @param a ascoltatore legato alla pressione del bottone.
			 */
			JPanelCluster(String buttonName, ActionListener a) {
				
				JPanel upPanel = new JPanel();
				JPanel centerPanel = new JPanel();
				JPanel downPanel = new JPanel();
				
				clusterOutput.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(clusterOutput);
				
				executeButton = new JButton(buttonName);
				executeButton.addActionListener(a);
				
				upPanel.setLayout(new FlowLayout());
				centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
				downPanel.setLayout(new FlowLayout());
				
				upPanel.add( new JLabel("table") );
				upPanel.add( tableText );
				upPanel.add( new JLabel("radius") );
				upPanel.add( parameterText );
				
				centerPanel.add(Box.createVerticalStrut(30));
				centerPanel.add( scrollPane );

				downPanel.add( executeButton );
				
				setLayout( new BorderLayout() );
				add( upPanel, BorderLayout.NORTH );
				add( centerPanel, BorderLayout.CENTER );
				add( downPanel, BorderLayout.SOUTH );
				
			}
		}
			
		/**
		 * Inizializza i membri panelDB e panelFile e li aggiunge ad un oggetto istanza della classe TabbedPane. 
		 * Tale oggetto (TabbedPane) � quindi inserito nel pannello che si sta costruendo.
		 */
		TabbedPane() {
			
			super(new GridLayout(1, 1)); 
			JTabbedPane tabbedPane = new JTabbedPane();
			
			java.net.URL imgURL = getClass().getResource("/img/db.jpg");
			ImageIcon iconDB = new ImageIcon(imgURL);
			
			panelDB = new JPanelCluster("MINE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						learningFromDBAction();
					}
					catch (SocketException e1) {
						JOptionPane.showMessageDialog(panelDB, e1.getMessage(), "Socket Exception", 0);
						
						
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(panelDB, e1.getMessage(), "File Not Found", 0);
					
						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(panelDB, e1.getMessage(), "IO Exception", 0);
						
						
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(panelDB, e1.getMessage(), "Class Not Found", 0);
					
					}
				
					
				}
		      });
	        
			tabbedPane.addTab("DB", iconDB, panelDB, "Learning from database");
			
	        imgURL = getClass().getResource("/img/file.jpg");
	        ImageIcon iconFile = new ImageIcon(imgURL);
	        
			panelFile = new JPanelCluster("STORE FROM FILE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						learningFromFileAction();
					}
					catch (SocketException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage(), "Socket Exception", 0);
						
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage(), "File Not Found", 0);
						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage(), "IO Exception", 0);
						
					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage(), "Class Not Found", 0);
						
					}
					
				}
		      });
			
	        tabbedPane.addTab("FILE", iconFile, panelFile, "Learning from file");
	        add(tabbedPane);         
	        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		
		/**
		 * Acquisisce il nome della tabella e il raggio dalle apposite caselle di testo e li invia al server.
		 * Qualora il valore del raggio acquisito non fosse un numero reale maggiore di zero visualizza un messaggio di errore all'interno 
		 * di un MessageDialog.
		 * Riceve dal server tutte le informazioni sui cluster scoperti e le mostra nella casella di testo appropriata (clusterOutput).
		 * Infine salva il risultato in un file nominato con nome della tabella inserito, raggio del cluster inserito e estensione .dmp.
		 * @throws SocketException nel caso di errore nell'accesso al socket.
		 * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
		 * @throws ClassNotFoundException nel caso in cui la deserializzazione dell'oggetto richieda di dover caricare altre classi non trovate.
		 */
		private void learningFromDBAction()throws SocketException,IOException,ClassNotFoundException{
			
			double radius; 
			
			try {
				
				radius = new Double(panelDB.parameterText.getText()).doubleValue(); 
				if (radius<=0)
					throw new NumberFormatException("Radius can't be <= 0");
			} catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, e.getMessage());
				return;
			}
			
			
			String tableName = panelDB.tableText.getText();
			if(tableName.isEmpty()) {
				JOptionPane.showMessageDialog(this,"Table field is empty");
				return;
			}
			
			out.writeObject(0);
			out.writeObject(tableName);
			
			String result = (String)in.readObject();
			if (!(result.equals("OK"))){
				JOptionPane.showMessageDialog(this, result);
				return;
			}
			else {
				out.writeObject(1);
				out.writeObject(radius);
				
				result = (String)in.readObject();
				
				if (!(result.equals("OK"))){
					JOptionPane.showMessageDialog(this, result);
					return;
				}
				else {
					panelDB.clusterOutput.setText("Number of Clusters:"+in.readObject()+"\n"+in.readObject());
					
					out.writeObject(2);
					result = (String)in.readObject();
					
					if (!(result.equals("OK"))){
						JOptionPane.showMessageDialog(this, result);
						return;
					}
					JOptionPane.showMessageDialog(this,"Operation successful!");
				}	
				
			}
			
		}

		/**
		 * Acquisisce il nome della tabella e il raggio dalle apposite caselle di testo e li invia al server. Qualora il valore acquisito non fosse 
		 * un numero reale maggiore di zero visualizza un messaggio di errore all'interno di un MessageDialog.
		 * Riceve dal server una stringa costituita da ciascun centroide per ogni elemento dell�insieme dei Cluster e la stampa 
		 * nell'apposita casella di testo (clusterOutput).
		 * @throws SocketException nel caso di errore nell'accesso al socket.
		 * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
		 * @throws ClassNotFoundException nel caso in cui la deserializzazione dell'oggetto richieda di dover caricare altre classi non trovate.
		 */
		private void learningFromFileAction() throws SocketException,IOException,ClassNotFoundException {
			
			double radius;
			
			try{
				radius = new Double(panelFile.parameterText.getText()).doubleValue(); 
				if (radius<=0)
					throw new NumberFormatException("Radius can't be <= 0");
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, e.getMessage());
				return;
			}
			
			String tableName = panelFile.tableText.getText();
			if(tableName.isEmpty()) {
				JOptionPane.showMessageDialog(this,"Table field is empty");
				return;
			}
				
			
			out.writeObject(3);
			out.writeObject(tableName);
			out.writeObject(radius);
			
			String result = (String)in.readObject();
			if(!(result.equals("OK"))){
				JOptionPane.showMessageDialog(this, result);
				return;
			}
			else {
				panelFile.clusterOutput.setText((String)in.readObject());
				JOptionPane.showMessageDialog(this,"Operation successful!");
			}
			
		}
	}

	/**
	 * Inizializza la componente grafica della applet istanziando un oggetto della classe JTabbedPane e 
	 * aggiungendolo al container della applet. Inoltre avvia la richiesta di connessione al Server ed 
	 * inizializza i flussi di comunicazione (membri dato in e out). Ip del server e porta sono acquisiti 
	 * come parametri dalla pagina HTML. Visualizza un messagio di errore in un MessageDialog nel caso in cui
	 * si verifichi il fallimento o la interruzione di una operazione di I/O.
	 */
	public void init() {
		
		TabbedPane tab = new TabbedPane();
		setSize(500, 380);
		getContentPane().setLayout(new GridLayout(1,1));
		getContentPane().add(tab);
		
		
		String ip = this.getParameter("ServerIP");
		int port= new Integer(this.getParameter("Port")).intValue();
		
		try {
			InetAddress addr = InetAddress.getByName(ip);
			System.out.println("addr = " + addr);
			Socket socket = new Socket(addr, port);
			System.out.println(socket);
		
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream()); 
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(tab, e.getMessage(), "IO Exception", 0);
			System.exit(0);
		}
	}
	
}
