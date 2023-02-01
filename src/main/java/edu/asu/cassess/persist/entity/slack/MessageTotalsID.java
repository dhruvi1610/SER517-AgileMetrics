package edu.asu.cassess.persist.entity.slack;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class MessageTotalsID implements Serializable {

        @Column(name = "email")
        public String email;

        @Column(name = "channel_id")
        public String channel_id;

        @Column(name = "retrievalDate")
        private String retrievalDate;

        public MessageTotalsID() {
        }

        public MessageTotalsID(String email, String channel_id) {
            this.email = email;
            this.channel_id = channel_id;
            Date date = new Date();
            this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getChannelID() {
            return channel_id;
        }

        public void setChannelID(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getRetrievalDate() {
            return retrievalDate;
        }

        public void setRetrievalDate() {
            Date date = new Date();
            this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }
