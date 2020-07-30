(ns ctray.core
 (:require [ctray.state :refer [*runtime-state]])
 (:import [java.awt AWTException]
          [java.awt Image]
          [java.awt MenuItem]
          [java.awt PopupMenu]
          [java.awt SystemTray]
          [java.awt TrayIcon]
          [java.awt.event ActionListener]
          [java.io File]
          [javax.imageio ImageIO]
          [java.util.prefs Preferences] )
    (:gen-class))

(defn tray-supported?
  []
  (. SystemTray isSupported))

(defn str->img [str](ImageIO/read (new File str)))

(defn make-tray ([]( do 
                 (let [tray (@*runtime-state :tray) ](if (nil?  (@*runtime-state :tray ) )
                                                           (swap! *runtime-state assoc :tray (SystemTray/getSystemTray))) ) ; if nil
                 (@*runtime-state :tray)
                   ))
                ([tray-icon](doto (@*runtime-state :tray) (.add tray-icon) )))


(defn make-tray-menu ([](make-tray-menu (str->img  "./resources/TrayIcon.png")))
                     ([image](let [
                                   popup-menu ( new PopupMenu)
                                   tray-icon ( new TrayIcon image  "trayIcon" popup-menu)
                                    ]
                                        (do (swap! *runtime-state assoc :popup-menu popup-menu)
                                            (swap! *runtime-state assoc :tray-icon  tray-icon )
                                              popup-menu
                                         )
                       )))
(defn make-menu-item ([str] ( new MenuItem str )
                     ))

(def exit-menu-item  (doto ( new MenuItem "EXIT" )(.addActionListener (reify ActionListener(actionPerformed [this e](do (prn e) (System/exit 0) ) ) ))
                     ))
(defn init[] ( do
                 (swap! *runtime-state assoc
                      :prefs (.node (Preferences/userRoot) "ctray"))
                 (let [tray          (make-tray)
                       default-item  exit-menu-item
                       popup-icon    (make-tray-menu)
                        ]
                              (do (.add popup-icon default-item)
                                  (.add tray  (@*runtime-state :tray-icon))
                              )
                   )
                ))

(defn init*[] (do
                 (swap! *runtime-state assoc
                      :prefs (.node (Preferences/userRoot) "ctray"))
                 (swap! *runtime-state assoc :tray (SystemTray/getSystemTray))
                 (let [image    (ImageIO/read (new File "./resources/TrayIcon.png" ))
                       exit-act (reify ActionListener(actionPerformed [this e](do (prn e) (System/exit 0) ) ) )
                       popup-menu ( new PopupMenu)
                       default-item ( new MenuItem "EXIT" )
                       tray-icon ( new TrayIcon image  "LeinRepl" popup-menu)
                        ]
                              (do (swap! *runtime-state assoc :popup-menu popup-menu)
                                  (.add popup-menu default-item)
                                  ;(.addActionListener tray-icon exit-act)
                                  (.addActionListener default-item exit-act)
                                  (.add (@*runtime-state :tray) tray-icon)
                                  (swap! *runtime-state assoc :tray-icon tray-icon)
                                  (swap! *runtime-state assoc :exit-act exit-act)
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

(defn main
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
