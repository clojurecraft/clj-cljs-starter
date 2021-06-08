(ns app.client.ajax
  (:require
    [lambdaisland.fetch :as fetch]
    [kitchen-async.promise :as p]
    [re-frame.core :refer [reg-fx dispatch]]))

(reg-fx :fetch
        (fn [opts]
          (-> (fetch/request (opts :url)
                             opts)
              (p/then (fn [response]
                        (if (<= 200 (:status response) 299)
                          (cond
                            (keyword? (opts :then))
                            (dispatch [(opts :then)
                                       (:body response)])
                            (fn? (opts :then))
                            ((opts :then) (:body response)))
                          (cond
                            (keyword? (opts :catch))
                            (dispatch [(opts :catch) (:bbo response)])
                            (fn? (opts :catch))
                            ((opts :catch) (:body response)))))))))
