//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represent a invoicing contact for an actor.
 */
@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoContact {

    /**
     * Full name or company name
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String name;

    /**
     * Tax identification, VAT ...
     */

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String taxIdentification;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String street;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String extras;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String postalCode;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String city;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String country;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String legalId;

    @Basic(optional = true)
    public BigDecimal taxRate;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String invoiceIdTemplate;

    @Basic(optional = true)
    public BigDecimal invoiceIdNumber;

    /**
     * Sets the tax identification.
     * 
     * @param taxIdentification tax identification
     */
    public void setTaxIdentification(final String taxIdentification) {
        this.taxIdentification = taxIdentification;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the tax identification.
     * 
     * @return the tax identification
     */
    public String getTaxIdentification() {
        return this.taxIdentification;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(final String extras) {
        this.extras = extras;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(final String legalId) {
        this.legalId = legalId;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(final BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getInvoiceIdTemplate() {
        return invoiceIdTemplate;
    }

    public void setInvoiceIdTemplate(final String invoiceIdTemplate) {
        this.invoiceIdTemplate = invoiceIdTemplate;
    }

    public BigDecimal getInvoiceIdNumber() {
        return invoiceIdNumber;
    }

    public void setInvoiceIdNumber(final BigDecimal invoiceIdNumber) {
        this.invoiceIdNumber = invoiceIdNumber;
    }

    // ======================================================================
    // Hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao bug.
     */
    protected DaoContact() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((extras == null) ? 0 : extras.hashCode());
        result = prime * result + ((invoiceIdNumber == null) ? 0 : invoiceIdNumber.hashCode());
        result = prime * result + ((invoiceIdTemplate == null) ? 0 : invoiceIdTemplate.hashCode());
        result = prime * result + ((legalId == null) ? 0 : legalId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((taxIdentification == null) ? 0 : taxIdentification.hashCode());
        result = prime * result + ((taxRate == null) ? 0 : taxRate.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoContact other = (DaoContact) obj;
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (country == null) {
            if (other.country != null) {
                return false;
            }
        } else if (!country.equals(other.country)) {
            return false;
        }
        if (extras == null) {
            if (other.extras != null) {
                return false;
            }
        } else if (!extras.equals(other.extras)) {
            return false;
        }
        if (invoiceIdNumber == null) {
            if (other.invoiceIdNumber != null) {
                return false;
            }
        } else if (!invoiceIdNumber.equals(other.invoiceIdNumber)) {
            return false;
        }
        if (invoiceIdTemplate == null) {
            if (other.invoiceIdTemplate != null) {
                return false;
            }
        } else if (!invoiceIdTemplate.equals(other.invoiceIdTemplate)) {
            return false;
        }
        if (legalId == null) {
            if (other.legalId != null) {
                return false;
            }
        } else if (!legalId.equals(other.legalId)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!postalCode.equals(other.postalCode)) {
            return false;
        }
        if (street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!street.equals(other.street)) {
            return false;
        }
        if (taxIdentification == null) {
            if (other.taxIdentification != null) {
                return false;
            }
        } else if (!taxIdentification.equals(other.taxIdentification)) {
            return false;
        }
        if (taxRate == null) {
            if (other.taxRate != null) {
                return false;
            }
        } else if (!taxRate.equals(other.taxRate)) {
            return false;
        }
        return true;
    }

}
