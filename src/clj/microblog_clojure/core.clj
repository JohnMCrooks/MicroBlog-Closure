(ns microblog-clojure.core
  (:require [compojure.core :as c]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as p]
            [ring.middleware.resource :refer [wrap-resource]]
            [hiccup.core :as h]
            [ring.adapter.jetty :as j]
            [ring.util.response :as r])
            
  (:gen-class))


(def ^:const port 3000)
(defonce server (atom nil))
(defonce messages (atom []))

(c/defroutes app
  (c/GET "/" request
    (h/html [:html
             [:head
              [:link {:rel "stylesheet" :href "page.css"}]]
             [:body
              [:form {:action "/add-message" :method "post"}
               [:input {:type "text" :placeholder "Enter Message" :name "message"}]
               [:button {:type "submit"} "Add message"]]
              [:ol 
               (map (fn [message]
                      [:li message])
                  @messages)]]]))
  
  (c/POST "/add-message" request
    (let [params (get request :params)
          message (get params "message")]
      (swap! messages conj message)
      (r/redirect "/"))))
            
             

(defn -main [& args]
  (when @server
    (.stop @server))
  (let [app(p/wrap-params app)]
    (reset! server
          (j/run-jetty app {:port port :join? false}))))
 
