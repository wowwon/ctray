(ns ctray.cmd
 (:require [ctray.state :refer [*pref-state *runtime-state init-pref-state!]]
           [seesaw.core])
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


(defn scpanel
   ( []
     ( let [ ta     (seesaw.core/text :id :commandinput  :multi-line? true    )
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
            enter-action (seesaw.core/action :name "Enter" :handler (fn[e](prn e) ))
            clear-action (seesaw.core/action :name "Clear" :handler (fn[e](config! (select cf [:#commandinput] ) :text "")  ))
            ]
            (do 
                (seesaw.core/config! enter :action enter-action)
                (seesaw.core/config! clear :action clear-action)
                bp
                )
            )
     )
)

(defn showscp   ([bp ] (showscp "SC"  ))
                ([bp title]
                 ( let [ cf     (seesaw.core/frame :title title)
                         scpanel(scpanel)
                        ]
                    (do
                     (seesaw.core/config! cf :content bp) 
                     (.setSize cf 300 300)
                     cf
                     )
                 )
                )
)


(defn- initcf
   ([] (initcf "SC"  ))
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
            enter-action (seesaw.core/action :name "Enter" :handler (fn[e](prn e) ))
            clear-action (seesaw.core/action :name "Clear" :handler (fn[e](config! (select cf [:#commandinput] ) :text "")  ))
            ]
            (do 
                (seesaw.core/config! enter :action enter-action)
                (seesaw.core/config! clear :action clear-action)
                (seesaw.core/config! cf :content bp) 
                (.setSize cf 300 300)
                cf
                )
            )
     )
)


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
