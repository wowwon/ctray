(ns ctray.cmd
 (:require [ctray.state :refer [*pref-state *runtime-state init-pref-state!]])
 (:import [javax.swing Box]
          [javax.swing BorderFactory]
          
          
          [java.awt AWTException]
          [java.awt Image]
          [java.awt MenuItem]
          [java.awt PopupMenu]
          [java.awt SystemTray]
          [java.awt TrayIcon]
          [java.awt.event ActionEvent]
          [java.awt.event ActionListener]
          [java.awt.image BufferedImage]
          [java.io File]
          [java.io IOException]
          [java.util ArrayList]
          [javax.imageio ImageIO]
          [javax.swing Box]
          [java.util.prefs Preferences] )
    (:gen-class))


(require '(clojure.java [io :as io]))

;; A common task it to load a file into a byte array.
(defn file->bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))


(require 'seesaw.core)
(seesaw.core/config! f :content content)

(defn init
   ([] (init "SC"  ))
   ( [title]
     ( let [ cf     (seesaw.core/frame :title title)
             ta     (seesaw.core/text :id :commandinput  :multi-line? true    )
             tf     (seesaw.core/text :id :commandoutput :multi-line? false   )
             enter  (seesaw.core/button :id :enter :text "Enter")
             clear  (seesaw.core/button :id :clear :text "Clear")
             bx     (doto (Box/createHorizontalBox)
                          (.setBorder (BorderFactory/createEmptyBorder 5 5 5 5 ))
                          (.add (Box/createHorizontalGlue))
                          (.add tf)
                          (.add (Box/createHorizontalStrut 5))
                          (.add enter)
                          (.add (Box/createHorizontalStrut 5))
                          (.add clear)
                         )
             bp  (seesaw.core/border-panel 
                 ;:north (horizontal-panel :items rbs)
                 :center ta
                 :vgap 5 :hgap 5 :border 5
                 :south bx
                 )
            enter-action (seesaw.core/action :id :enter-action :handler (fn[e](prn e) ))
            clear-action (seesaw.core/action :id :clear-action :handler (fn[e](prn e) ))
            ]
            (do (seesaw.core/config! cf :content bp))
            )
     )
)
(def watchflg (atom true))
(defn make-console[] (
                      let [pb (new ProcessBuilder []) 
                           scp (:Textarea )
                           textArea  = new JTextArea();
                           textField = new JTextField("");
                           enterbtn  = new JButton("Enter");
                               Box box = Box.createHorizontalBox();
    box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    box.add(Box.createHorizontalGlue());
    box.add(textField);
    box.add(Box.createHorizontalStrut(5));
    box.add(enterbtn);
    box.add(Box.createHorizontalStrut(5));
    box.add(button);

    add(new JScrollPane(textArea));
    add(box, BorderLayout.SOUTH);
    
                           pwatch 
                      
                      
                      
                      
                                                       ] (
                      )))
 
(defn init[] (do
                 (swap! *runtime-state assoc
                      :prefs (.node (Preferences/userRoot) "ctray"))
                 (swap! *runtime-state assoc :tray (SystemTray/getSystemTray))
                 (let [image    (ImageIO/read (new File "./resources/TrayIcon.png" ))
                       exit_act (reify ActionListener(actionPerformed [this e](do (prn e) (System/exit 0) ) ) )  
                       popup_menu ( new PopupMenu)
                       defaultItem ( new MenuItem "EXIT" )
                       trayIcon ( new TrayIcon image  "LeinRepl" popup_menu)
                        ]
                              (do (swap! *runtime-state assoc :popup_menu popup_menu)
                                  (.add popup_menu defaultItem)
                                  ;(.addActionListener trayIcon exit_act)
                                  (.addActionListener defaultItem exit_act)
                                  (.add (@*runtime-state :tray) trayIcon)
                                  (swap! *runtime-state assoc :trayIcon trayIcon)
                                  (swap! *runtime-state assoc :exit_act exit_act)
                              )
                   )
                 ))

(defn initConsole[titlename](let [  titlestr (if keyword? (name titlename) titlename )
                                    titlekey (if keyword?  titlename (keyword titlename))
                                    menu_iem ( new MenuItem titlestr )
                                 ]
                             (prn :exit)
                            )
)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
