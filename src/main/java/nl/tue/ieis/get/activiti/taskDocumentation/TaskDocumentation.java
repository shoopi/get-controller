package main.java.nl.tue.ieis.get.activiti.taskDocumentation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import main.java.nl.tue.ieis.get.event.type.EventType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "replanningAnnotation",
    "queryAnnotation"
})
@XmlRootElement(name = "taskDocumentation")
public class TaskDocumentation {

    protected TaskDocumentation.ReplanningAnnotation replanningAnnotation;
    protected TaskDocumentation.QueryAnnotation queryAnnotation;

    public TaskDocumentation.ReplanningAnnotation getReplanningAnnotation() {
        return replanningAnnotation;
    }

    public void setReplanningAnnotation(TaskDocumentation.ReplanningAnnotation value) {
        this.replanningAnnotation = value;
    }

    public TaskDocumentation.QueryAnnotation getQueryAnnotation() {
        return queryAnnotation;
    }

    public void setQueryAnnotation(TaskDocumentation.QueryAnnotation value) {
        this.queryAnnotation = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "query"
    })
    public static class QueryAnnotation {

        protected List<TaskDocumentation.QueryAnnotation.Query> query;

        public List<TaskDocumentation.QueryAnnotation.Query> getQuery() {
            if (query == null) {
                query = new ArrayList<TaskDocumentation.QueryAnnotation.Query>();
            }
            return this.query;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Query {

            @XmlElement(required = true)
            protected String queryText;
           
            @XmlElement(required = true)
            protected String eventType;
            
            @XmlElement(required = true)
            protected TaskDocumentation.QueryAnnotation.Query.Scope scope;
            
            private EventType eventTypeEnum;
            private String email;
            
    		public EventType getEventTypeEnum() {
    			return eventTypeEnum;
    		}

    		public void setEventTypeEnum(EventType eventTypeEnum) {
    			this.eventTypeEnum = eventTypeEnum;
    		}
    		
    		public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			public String getQueryText() {
                return queryText;
            }

            public void setQueryText(String value) {
                this.queryText = value;
            }
            
            public String getEventType() {
                return eventType;
            }
            
            public void setEventType(String value) {
            	this.eventType = value.trim();
                this.eventTypeEnum = EventType.valueOf(eventType);
            }
            
            
            public TaskDocumentation.QueryAnnotation.Query.Scope getScope() {
                return scope;
            }

            public void setScope(TaskDocumentation.QueryAnnotation.Query.Scope value) {
                this.scope = value;
            }


            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "startTask",
                "endTask",
                "roles"
            })
            public static class Scope {

                @XmlElement(required = true)
                protected String startTask;
                protected List<String> endTask;
                protected String roles;
                @XmlAttribute(name = "trigger")
                protected Boolean trigger;

                public String getStartTask() {
                    return startTask;
                }

                public void setStartTask(String value) {
                    this.startTask = value;
                }

                public List<String> getEndTask() {
                    if (endTask == null) {
                        endTask = new ArrayList<String>();
                    }
                    return this.endTask;
                }

                public String getRoles() {
                    return roles;
                }

                public void setRoles(String value) {
                    this.roles = value;
                }

                public Boolean isTrigger() {
                    return trigger;
                }

                public void setTrigger(Boolean value) {
                    this.trigger = value;
                }

				@Override
				public int hashCode() {
					final int prime = 31;
					int result = 1;
					result = prime * result
							+ ((endTask == null) ? 0 : endTask.hashCode());
					result = prime * result
							+ ((roles == null) ? 0 : roles.hashCode());
					result = prime * result
							+ ((startTask == null) ? 0 : startTask.hashCode());
					result = prime * result
							+ ((trigger == null) ? 0 : trigger.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (!(obj instanceof Scope))
						return false;
					Scope other = (Scope) obj;
					if (endTask == null) {
						if (other.endTask != null)
							return false;
					} else if (!endTask.equals(other.endTask))
						return false;
					if (roles == null) {
						if (other.roles != null)
							return false;
					} else if (!roles.equals(other.roles))
						return false;
					if (startTask == null) {
						if (other.startTask != null)
							return false;
					} else if (!startTask.equals(other.startTask))
						return false;
					if (trigger == null) {
						if (other.trigger != null)
							return false;
					} else if (!trigger.equals(other.trigger))
						return false;
					return true;
				}
            }


			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((email == null) ? 0 : email.hashCode());
				result = prime
						* result
						+ ((eventTypeEnum == null) ? 0 : eventTypeEnum
								.hashCode());
				result = prime * result
						+ ((queryText == null) ? 0 : queryText.hashCode());
				result = prime * result
						+ ((scope == null) ? 0 : scope.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (!(obj instanceof Query))
					return false;
				Query other = (Query) obj;
				if (email == null) {
					if (other.email != null)
						return false;
				} else if (!email.equals(other.email))
					return false;
				if (eventTypeEnum != other.eventTypeEnum)
					return false;
				if (queryText == null) {
					if (other.queryText != null)
						return false;
				} else if (!queryText.equals(other.queryText))
					return false;
				if (scope == null) {
					if (other.scope != null)
						return false;
				} else if (!scope.equals(other.scope))
					return false;
				return true;
			}

			
        }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((query == null) ? 0 : query.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof QueryAnnotation))
				return false;
			QueryAnnotation other = (QueryAnnotation) obj;
			if (query == null) {
				if (other.query != null)
					return false;
			} else if (!query.equals(other.query))
				return false;
			return true;
		}
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "source",
        "destination",
        "isRollbackable",
        "isConfirmationTask",
        "compensatoryTask",
        "confirmationTask"
    })
    public static class ReplanningAnnotation {

        protected String source;
        protected String destination;
        protected Boolean isRollbackable;
        protected Boolean isConfirmationTask;
        protected String compensatoryTask;
        protected String confirmationTask;

        public String getSource() {
            return source;
        }

        public void setSource(String value) {
            this.source = value;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String value) {
            this.destination = value;
        }

        public Boolean isIsRollbackable() {
            return isRollbackable;
        }
   
        public void setIsRollbackable(Boolean value) {
            this.isRollbackable = value;
        }

        public Boolean isIsConfirmationTask() {
            return isConfirmationTask;
        }

        public void setIsConfirmationTask(Boolean value) {
            this.isConfirmationTask = value;
        }

        public String getCompensatoryTask() {
            return compensatoryTask;
        }

        public void setCompensatoryTask(String value) {
            this.compensatoryTask = value;
        }

        public String getConfirmationTask() {
            return confirmationTask;
        }

        public void setConfirmationTask(String value) {
            this.confirmationTask = value;
        }

    }

}
