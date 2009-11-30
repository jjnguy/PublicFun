using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace PackageTrackingService
{
	public class OrderService : IOrderService
	{
		public string GetData(int value)
		{
			return string.Format("You entered: {0}", value);
		}

		public CompositeType GetDataUsingDataContract(CompositeType composite)
		{
			if (composite.BoolValue)
			{
				composite.StringValue += "Suffix";
			}
			return composite;
		}

		#region IOrderService Members

		public string HelloWho(string name)
		{
			return "Hello " + name;
		}

		public int Square(int i)
		{
			return i*i;
		}

		#endregion
	}
}
