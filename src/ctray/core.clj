(ns ctray.core
 (:require [ctray.state :refer [*pref-state *runtime-state init-pref-state!]])
 (:import [java.awt AWTException]
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
          [java.util.prefs Preferences] )
    (:gen-class))
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
                                  (.addActionListener trayIcon exit_act)
                                  (.addActionListener defaultItem exit_act)
                                  (.add (@*runtime-state :tray) trayIcon)
                                  (swap! *runtime-state assoc :trayIcon trayIcon)
                                  (swap! *runtime-state assoc :exit_act exit_act)
                              )
                   )
                 ))
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
